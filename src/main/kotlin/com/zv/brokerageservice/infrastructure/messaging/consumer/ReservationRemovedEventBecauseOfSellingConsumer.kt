package com.zv.brokerageservice.infrastructure.messaging.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.ReservationRemovedBecauseOfSelling
import com.zv.brokerageservice.infrastructure.messaging.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReservationRemovedEventBecauseOfSellingConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(ReservationRemovedEventBecauseOfSellingConsumer::class.java)

    @KafkaListener(
        topics = ["#{@reservationRemovedBecauseOfSellingEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received ReservationRemoved ${cr.value()}")
        val reservationRemovedBecauseOfSelling = objectMapper.readValue(cr.value(), ReservationRemovedBecauseOfSelling::class.java)
        commandBus.publishNotificationAsync(reservationRemovedBecauseOfSelling)
    }
}

