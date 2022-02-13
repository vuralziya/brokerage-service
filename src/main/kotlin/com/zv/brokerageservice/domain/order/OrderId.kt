package com.zv.brokerageservice.domain.order

import java.util.*

class OrderId(val value: String) {

    companion object {
        fun create(): OrderId {
            return OrderId(UUID.randomUUID().toString())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}