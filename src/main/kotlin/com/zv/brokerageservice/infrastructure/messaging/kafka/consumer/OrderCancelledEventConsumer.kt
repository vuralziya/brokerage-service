package com.zv.brokerageservice.infrastructure.messaging.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.OrderCancelled
import com.zv.brokerageservice.application.notification.OrderCompleted
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderCancelledEventConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(OrderCancelledEventConsumer::class.java)

    @KafkaListener(
        topics = ["#{@orderCancelledEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received OrderCancelled ${cr.value()}")
        val orderCancelled = objectMapper.readValue(cr.value(), OrderCancelled::class.java)
        commandBus.publishNotificationAsync(orderCancelled)
    }
}

