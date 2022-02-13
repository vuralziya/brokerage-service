package com.zv.brokerageservice.infrastructure.api.response

data class UserDto(
    val name: String,
    val lastName: String,
    val identityNumber: Long,
    val citizen: String
)
