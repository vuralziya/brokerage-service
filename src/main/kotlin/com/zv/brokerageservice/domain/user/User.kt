package com.zv.brokerageservice.domain.user

import com.zv.brokerageservice.domain.AggregateRoot
import com.zv.brokerageservice.domain.event.OrderAddedToUser
import com.zv.brokerageservice.domain.event.UserCreated

class User private constructor(
    id: String,
    val name: String,
    val lastName: String,
    val identityNumber: Long,
    val citizen: String
) : AggregateRoot<User>(id) {

    override val type: String
        get() = TYPE

    var orders = mutableListOf<String>()
        private set

    companion object {
        const val TYPE = "User"
        fun create(
            name: String,
            lastName: String,
            identityNumber: Long,
            citizen: String
        ): User {
            val firstName = Name.create(name)
            val surName = Name.create(lastName)
            val userId = UserId.create(identityNumber, citizen)
            val id = userId.value
            val user = User(id, firstName.value, surName.value, identityNumber, citizen)
            user.registerEvent(UserCreated(id))
            return user
        }

        fun from(
            id: String,
            name: String,
            lastName: String,
            identityNumber: Long,
            citizen: String,
            orders: List<String>
        ): User {
            val user = User(id, name, lastName, identityNumber, citizen)
            user.orders = orders.toMutableList()
            return user
        }
    }

    fun addOrder(orderId: String) {
        if (orders.contains(orderId)) {
            return
        }
        orders.add(orderId)
        registerEvent(OrderAddedToUser(id, orderId))
    }
}



