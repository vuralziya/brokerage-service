package com.zv.brokerageservice.infrastructure.config.binding

import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.command.CreateStock
import com.zv.brokerageservice.infrastructure.messaging.kafka.TopicConfig
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.kafka.config.TopicBuilder

@Configuration
class StartupConfiguration(
    private val topicConfigs: List<TopicConfig>,
    private val kafkaProperties: KafkaProperties,
    private val commandBus: CommandBus
) {

    @EventListener(ApplicationReadyEvent::class)
    fun initTopics() {
        val adminClient =
            AdminClient.create(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers))
        val topics = topicConfigs.map {
            TopicBuilder.name(it.topic)
                .build()
        }
        adminClient.createTopics(topics)
        adminClient.close()
    }

    @EventListener(ApplicationReadyEvent::class)
    fun createInitialStock() = runBlocking {
        commandBus.executeCommandAsync(CreateStock("APPLE", 10))
    }
}
