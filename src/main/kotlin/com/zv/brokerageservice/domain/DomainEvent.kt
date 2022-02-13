package com.zv.brokerageservice.domain

import com.zv.brokerageservice.domain.event.EventType

abstract class DomainEvent {
    abstract val type: EventType
    var version: Long = 0
}
