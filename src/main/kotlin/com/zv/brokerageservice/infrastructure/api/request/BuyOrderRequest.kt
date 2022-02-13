package com.zv.brokerageservice.infrastructure.api.request

data class BuyOrderRequest(
    val userId: String,
    val stockId: String,
    val quantity: Int
)