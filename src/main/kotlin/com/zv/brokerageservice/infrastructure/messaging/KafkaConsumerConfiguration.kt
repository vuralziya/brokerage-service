package com.zv.brokerageservice.infrastructure.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class KafkaConsumerConfiguration(
    private val kafkaProperties: KafkaProperties,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun kafkaListenerContainerFactory(kafkaTemplate: KafkaTemplate<String, Any>): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        factory.setMessageConverter(stringJsonMessageConverter())
        return factory
    }

    private fun consumerFactory(): ConsumerFactory<String, Any> {
        return DefaultKafkaConsumerFactory(consumerConfig())
    }

    fun consumerConfig(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = "5000"
        props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "45000"
        props[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "3000"
        props[ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG] = false
        return props
    }

    @Bean
    fun stringJsonMessageConverter(): StringJsonMessageConverter {
        return StringJsonMessageConverter(objectMapper)
    }

    companion object {
        const val LISTENER_BEAN_NAME = "kafkaListenerContainerFactory"
    }
}
