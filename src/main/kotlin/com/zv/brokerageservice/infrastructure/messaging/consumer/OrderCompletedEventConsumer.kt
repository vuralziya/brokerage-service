package com.zv.brokerageservice.infrastructure.messaging.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.OrderCompleted
import com.zv.brokerageservice.infrastructure.messaging.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderCompletedEventConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(OrderCompletedEventConsumer::class.java)

    @KafkaListener(
        topics = ["#{@orderCompletedEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received OrderCompleted ${cr.value()}")
        val orderCompleted = objectMapper.readValue(cr.value(), OrderCompleted::class.java)
        commandBus.publishNotificationAsync(orderCompleted)
    }
}

