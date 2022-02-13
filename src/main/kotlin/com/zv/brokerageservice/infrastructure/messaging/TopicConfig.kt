package com.zv.brokerageservice.infrastructure.messaging

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers")
class ConsumerConfig(var groupId: String = "")

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.buy-order-request-received-event")
class BuyOrderRequestReceivedEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.sell-order-request-received-event")
class SellOrderRequestReceivedEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.cancel-order-request-received-event")
class CancelOrderRequestReceivedEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.order-created-as-initial-event")
class OrderCreatedAsInitialEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.order-added-to-user-event")
class OrderAddedToUserTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.reservation-completed-event")
class ReservationCompletedEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.reservation-removed-because-of-selling-event")
class ReservationRemovedBecauseOfSellingEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.order-completed-event")
class OrderCompletedEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.order-selling-started-event")
class OrderSellingStartedEventTopicConfig : TopicConfig()

@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.order-sold-event")
class OrderSoldEventTopicConfig : TopicConfig()

abstract class TopicConfig(
    var topic: String = ""
)
