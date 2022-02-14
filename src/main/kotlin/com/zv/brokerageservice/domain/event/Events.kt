package com.zv.brokerageservice.domain.event

import com.zv.brokerageservice.domain.DomainEvent

data class BuyOrderRequestReceived(
    val userId: String,
    val stockId: String,
    val quantity: Int
) : DomainEvent() {
    override val type: EventType
        get() = EventType.BUY_ORDER_REQUEST_RECEIVED
}

data class SellOrderRequestReceived(
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.SELL_ORDER_REQUEST_RECEIVED
}

data class CancelOrderRequestReceived(
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.CANCEL_ORDER_REQUEST_RECEIVED
}

data class UserCreated(
    val userId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.USER_CREATED
}

data class OrderCreatedAsInitial(
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_CREATED_AS_INITIAL
}

data class OrderAddedToUser(
    val userId: String,
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_ADDED_TO_USER
}

data class ReservationCompleted(
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.RESERVATION_COMPLETED
}

data class ReservationRemovedBecauseOfSelling(
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.RESERVATION_REMOVED_BECAUSE_OF_SELLING
}

data class ReservationRemovedBecauseOfCancellation(
    val orderId: String
) : DomainEvent() {
    override val type: EventType
        get() = EventType.RESERVATION_REMOVED_BECAUSE_OF_CANCELLATION
}

data class OrderCompleted(val orderId: String) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_COMPLETED
}

data class OrderSellingStarted(val orderId: String) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_SELLING_STARTED
}

data class OrderSold(val orderId: String) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_SOLD
}

data class OrderCancellingStarted(val orderId: String) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_CANCELLING_STARTED
}

data class OrderCancelled(val orderId: String) : DomainEvent() {
    override val type: EventType
        get() = EventType.ORDER_CANCELLED
}

data class StockCreated(val stockId: String) : DomainEvent() {
    override val type: EventType
        get() = EventType.STOCK_CREATED
}
