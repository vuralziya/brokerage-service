package com.zv.brokerageservice.infrastructure.messaging.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.CancelOrderRequestReceived
import com.zv.brokerageservice.infrastructure.messaging.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class CancelOrderRequestReceivedEventConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(CancelOrderRequestReceivedEventConsumer::class.java)

    @KafkaListener(
        topics = ["#{@cancelOrderRequestReceivedEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received BuyOrderRequestReceived event ${cr.value()}")
        val cancelOrderRequestReceived = objectMapper.readValue(cr.value(), CancelOrderRequestReceived::class.java)
        commandBus.publishNotificationAsync(cancelOrderRequestReceived)
    }
}

