package com.zv.brokerageservice.infrastructure.messaging

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
class KafkaProperties {
    var bootstrapServers: String = ""
    var acks: String = "1"
    var topicPrefix : String = ""
    var compression : String = "zstd"
}

