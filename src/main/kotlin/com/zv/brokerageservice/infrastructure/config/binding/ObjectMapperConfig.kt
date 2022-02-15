package com.zv.brokerageservice.infrastructure.config.binding

import com.couchbase.client.java.json.JsonValueModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.trendyol.productpreparationservice.infrastructure.config.binding.IsoInstantDeserializer
import com.trendyol.productpreparationservice.infrastructure.config.binding.IsoInstantSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

@Configuration
class ObjectMapperConfig {

    companion object {
        fun createObjectMapperWithDefaults(): ObjectMapper {
            val isoInstantModule = SimpleModule()
                .addSerializer(Instant::class.java, IsoInstantSerializer())
                .addDeserializer(Instant::class.java, IsoInstantDeserializer())

            return ObjectMapper()
                .registerKotlinModule()
                .registerModule(isoInstantModule)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        fun createObjectMapperForCouchbase(): ObjectMapper {
            return createObjectMapperWithDefaults()
                .registerModule(JsonValueModule())
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return createObjectMapperWithDefaults()
    }
    @Bean
    fun webClientObjectMapper(): ObjectMapper {
        return createObjectMapperWithDefaults()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
    }
}
