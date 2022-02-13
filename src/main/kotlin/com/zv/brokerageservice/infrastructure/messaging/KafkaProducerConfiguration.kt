package com.zv.brokerageservice.infrastructure.messaging

import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.ProducerListener

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class KafkaProducerConfiguration(
    private val kafkaProperties: KafkaProperties
) {
    private val logger = LoggerFactory.getLogger(javaClass)


    @Bean
    fun producer(): Producer<String, Any> {
        return producerFactory().createProducer()
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        val kafkaTemplate = KafkaTemplate(producerFactory())
        kafkaTemplate.setProducerListener(object : ProducerListener<String, Any> {
            override fun onError(
                producerRecord: ProducerRecord<String, Any>,
                recordMetadata: RecordMetadata?,
                exception: java.lang.Exception?
            ) {
                logger.error(
                    "ProducerListener Topic: ${producerRecord.topic()}, Key: ${producerRecord.value()}",
                    exception
                )
                super.onError(producerRecord, recordMetadata, exception)
            }
        })
        return kafkaTemplate
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    fun producerConfig(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.ACKS_CONFIG] = kafkaProperties.acks
        props[ProducerConfig.COMPRESSION_TYPE_CONFIG] = kafkaProperties.compression

        return props
    }


}
