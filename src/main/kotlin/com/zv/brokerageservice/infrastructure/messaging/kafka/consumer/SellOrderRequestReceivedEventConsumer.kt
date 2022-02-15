package com.zv.brokerageservice.infrastructure.messaging.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.SellOrderRequestReceived
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class SellOrderRequestReceivedEventConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(SellOrderRequestReceivedEventConsumer::class.java)

    @KafkaListener(
        topics = ["#{@sellOrderRequestReceivedEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received SellOrderRequestReceived event ${cr.value()}")
        val sellOrderRequestReceived = objectMapper.readValue(cr.value(), SellOrderRequestReceived::class.java)
        commandBus.publishNotificationAsync(sellOrderRequestReceived)
    }
}

