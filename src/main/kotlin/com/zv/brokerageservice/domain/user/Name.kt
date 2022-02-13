package com.zv.brokerageservice.domain.user

import com.zv.brokerageservice.domain.DomainException

class Name private constructor(val value: String) {
    companion object {
        fun create(value: String): Name {
            if (!allCharsValid(value)) {
                throw DomainException("name.contains.invalid.chars")
            }
            return Name(value)
        }

        private fun allCharsValid(value: String) = value.all {
            Character.isLetter(it) ||
                    Character.isWhitespace(it)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Name

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}