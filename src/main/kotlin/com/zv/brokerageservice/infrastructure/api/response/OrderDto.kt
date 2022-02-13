package com.zv.brokerageservice.infrastructure.api.response

import com.zv.brokerageservice.domain.order.Item

data class OrderDto(
    val item: Item
)
