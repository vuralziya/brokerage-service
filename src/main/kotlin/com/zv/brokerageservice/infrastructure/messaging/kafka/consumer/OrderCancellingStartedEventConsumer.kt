package com.zv.brokerageservice.infrastructure.messaging.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.OrderCancellingStarted
import com.zv.brokerageservice.application.notification.OrderSellingStarted
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderCancellingStartedEventConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(OrderSellingStartedEventConsumer::class.java)

    @KafkaListener(
        topics = ["#{@orderCancellingStartedEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received OrderCancellingStartedEvent ${cr.value()}")
        val orderCancellingStarted = objectMapper.readValue(cr.value(), OrderCancellingStarted::class.java)
        commandBus.publishNotificationAsync(orderCancellingStarted)
    }
}