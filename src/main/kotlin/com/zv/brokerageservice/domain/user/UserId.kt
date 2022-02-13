package com.zv.brokerageservice.domain.user

import com.zv.brokerageservice.domain.DomainException

class UserId private constructor(val value: String) {

    companion object {
        fun create(identityNumber: Long, citizen: String): UserId {
            if (!isInRange(identityNumber)) {
                throw DomainException("user.id.is.not.in.valid.range", identityNumber)
            }
            if (!allCharsValid(citizen)) {
                throw DomainException("citizen.contains.invalid.chars", citizen)
            }
            return UserId("${citizen}_${identityNumber}")
        }

        private fun isInRange(value: Long) = value.toString().length in 1..30
        private fun allCharsValid(value: String) = value.all {
            Character.isLetter(it) ||
                    Character.isWhitespace(it)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}