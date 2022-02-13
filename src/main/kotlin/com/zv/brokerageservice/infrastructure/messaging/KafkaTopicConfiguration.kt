package com.zv.brokerageservice.infrastructure.messaging

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfiguration(private val topicConfigs: List<TopicConfig>, private val kafkaProperties: KafkaProperties) {

    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        val adminClient =
            AdminClient.create(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers))
        val topics = topicConfigs.map {
            TopicBuilder.name(it.topic)
                .build()
        }
        adminClient.createTopics(topics)
        adminClient.close()
    }
}
