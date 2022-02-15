package com.zv.brokerageservice.infrastructure.messaging.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.OrderAddedToUser
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderAddedToUserEventConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(OrderAddedToUserEventConsumer::class.java)

    @KafkaListener(
        topics = ["#{@orderAddedToUserTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}-reservation",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received OrderAddedToUser ${cr.value()}")
        val orderAddedToUser = objectMapper.readValue(cr.value(), OrderAddedToUser::class.java)
        commandBus.publishNotificationAsync(orderAddedToUser)
    }

}
