package com.zv.brokerageservice.infrastructure.api.request

data class CreateUserRequest(
    val firstName: String,
    val lastName: String,
    val identityNumber: Long,
    val citizen: String
)
