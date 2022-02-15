package com.zv.brokerageservice.infrastructure.messaging.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.KafkaException
import org.apache.kafka.common.header.internals.RecordHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class KafkaOutgoingMessage(
    val topic: String,
    val key: String?,
    val payload: String,
    val headers: Map<String, String> = emptyMap(),
    val partition: Int? = null
)


@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : DisposableBean {
    private val logger: Logger = LoggerFactory.getLogger(KafkaProducer::class.java)

    suspend fun sendAll(messages: List<KafkaOutgoingMessage>): Boolean {
        val now = Instant.now().toEpochMilli()
        val result = suspendCoroutine<Boolean> {
            if (messages.isEmpty()) {
                it.resume(true)
            }

            val messageLength = AtomicInteger(messages.size)

            val callback = object : ListenableFutureCallback<SendResult<String, Any>?> {
                override fun onSuccess(result: SendResult<String, Any>?) {
                    val v = messageLength.decrementAndGet()
                    if (v == 0) {
                        it.resume(true)
                    }
                }

                override fun onFailure(exception: Throwable) {
                    logger.error("Error while publishing message to kafka:", exception)
                    it.resumeWithException(exception)
                }
            }

            var index = 0
            while (index < messages.size) {
                val (topic, key, payload, headers, partition) = messages[index]

                val kafkaHeaders = headers.map { map -> RecordHeader(map.key, map.value.toByteArray()) }.toMutableList()
                val record = ProducerRecord<String, Any>(topic, partition, now, key, payload, kafkaHeaders)
                try {
                    val sendResult = kafkaTemplate.send(record)
                    sendResult.addCallback(callback)
                } catch (ex: KafkaException) {
                    logger.error("Error occurred while sending", ex)
                    it.resumeWithException(ex)
                } catch (ex: Exception) {
                    logger.error("Error occurred while sending", ex)
                    it.resumeWithException(ex)
                }
                index++
            }
        }

        return result
    }

    override fun destroy() {
        kafkaTemplate.flush()
    }
}
