package com.zv.brokerageservice.domain.order

import com.zv.brokerageservice.domain.AggregateRoot
import com.zv.brokerageservice.domain.DomainException
import com.zv.brokerageservice.domain.event.*

class Order private constructor(
    id: String,
    val userId: String,
    val item: Item,
    var status: OrderStatus = OrderStatus.INITIAL
) : AggregateRoot<Order>(id) {

    override val type: String
        get() = TYPE

    companion object {
        const val TYPE = "Order"
        fun create(
            orderId: OrderId,
            userId: String,
            stockId: String,
            quantity: Int
        ): Order {
            val item = Item(stockId, quantity)
            val order = Order(orderId.value, userId, item)
            order.registerEvent(OrderCreatedAsInitial(order.id))
            return order
        }

        fun from(
            id: String,
            userId: String,
            item: Item,
            status: String
        ): Order {
            return Order(id, userId, item, OrderStatus.valueOf(status))
        }
    }

    fun complete() {
        if (status == OrderStatus.COMPLETED) {
            return
        }
        if (status != OrderStatus.INITIAL) {
            throw DomainException("oder.status.should.be.initial.in.order.to.complete")
        }
        status = OrderStatus.COMPLETED
        registerEvent(OrderCompleted(id))
    }

    fun sell() {
        if (status != OrderStatus.COMPLETED) {
            throw DomainException("not.completed.orders.can.not.be.sell")
        }
        status = OrderStatus.SELLING
        registerEvent(OrderSellingStarted(id))
    }

    fun completeSelling() {
        if (status == OrderStatus.SOLD) {
            return
        }
        if (status != OrderStatus.SELLING) {
            throw DomainException("oder.status.should.be.selling.in.order.to.sell")
        }
        status = OrderStatus.SOLD
        registerEvent(OrderSold(id))
    }

    fun cancel() {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.SOLD ) {
            throw DomainException("completed.orders.can.not.be.cancel")
        }
        status = OrderStatus.CANCELLING
        registerEvent(OrderCancellingStarted(id))
    }

    fun completeCancellation() {
        if (status == OrderStatus.CANCELLED) {
            return
        }
        status = OrderStatus.CANCELLED
        registerEvent(OrderCancelled(id))
    }
}

enum class OrderStatus {
    INITIAL, COMPLETED, SELLING, SOLD, CANCELLING, CANCELLED
}
