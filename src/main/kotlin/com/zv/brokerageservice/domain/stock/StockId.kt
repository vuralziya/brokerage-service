package com.zv.brokerageservice.domain.stock

import java.util.*

class StockId(val value: String) {

    companion object {
        fun create(): StockId {
            val value = "${Stock.TYPE}:${UUID.randomUUID()}"
            return StockId(value)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StockId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}