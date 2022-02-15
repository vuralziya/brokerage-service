package com.zv.brokerageservice.infrastructure.messaging.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.notification.ReservationRemovedBecauseOfCancellation
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaConsumerConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReservationRemovedBecauseOfCancellationConsumer(
    private val objectMapper: ObjectMapper,
    private val commandBus: CommandBus
) {

    private val logger = LoggerFactory.getLogger(ReservationRemovedBecauseOfCancellationConsumer::class.java)

    @KafkaListener(
        topics = ["#{@reservationRemovedBecauseOfCancellationEventTopicConfig.topic}"],
        groupId = "#{@consumerConfig.groupId}",
        containerFactory = KafkaConsumerConfiguration.LISTENER_BEAN_NAME,
    )
    fun listen(cr: ConsumerRecord<String, String>) = runBlocking {
        logger.info("Received ReservationRemoved ${cr.value()}")
        val reservationRemovedBecauseOfCancellation =
            objectMapper.readValue(cr.value(), ReservationRemovedBecauseOfCancellation::class.java)
        commandBus.publishNotificationAsync(reservationRemovedBecauseOfCancellation)
    }
}