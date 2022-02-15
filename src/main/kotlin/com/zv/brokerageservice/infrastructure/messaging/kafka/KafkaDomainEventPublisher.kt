package com.zv.brokerageservice.infrastructure.messaging.kafka

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.zv.brokerageservice.application.exception.ApplicationException
import com.zv.brokerageservice.domain.DomainEvent
import org.springframework.stereotype.Component


@Component
class KafkaDomainEventPublisher(
    val kafkaProducer: KafkaProducer,
    val objectMapper: ObjectMapper,
    val kafkaProperties: KafkaProperties
) {

    suspend fun publish(event: DomainEvent) {
        publishForAggregate(listOf(event))
    }

    suspend fun publishForAggregate(events: Collection<DomainEvent>, aggregateId: String? = null) {
        val outgoingMessages = events.map { event ->
            val topicName =
                "${kafkaProperties.topicPrefix}.${event.type.value}"
            val payload = serializePayload(event)
            KafkaOutgoingMessage(topicName, aggregateId, payload)
        }

        kafkaProducer.sendAll(outgoingMessages)
    }

    private fun serializePayload(event: DomainEvent): String {
        try {
            return objectMapper.writeValueAsString(event)
        } catch (ex: JsonProcessingException) {
            throw ApplicationException(ex.toString(), "an error occurred while serialization event!")
        }
    }
}