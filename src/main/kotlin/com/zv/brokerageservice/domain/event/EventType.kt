package com.zv.brokerageservice.domain.event

enum class EventType(val value: String) {
    BUY_ORDER_REQUEST_RECEIVED("buy.order.request.received"),
    SELL_ORDER_REQUEST_RECEIVED("sell.order.request.received"),
    CANCEL_ORDER_REQUEST_RECEIVED("cancel.order.request.received"),
    USER_CREATED("user.created"),
    ORDER_CREATED_AS_INITIAL("order.created.as.initial"),
    ORDER_ADDED_TO_USER("order.added.to.user"),
    RESERVATION_COMPLETED("reservation.completed"),
    RESERVATION_REMOVED_BECAUSE_OF_SELLING("reservation.removed.because.of.selling"),
    RESERVATION_REMOVED_BECAUSE_OF_CANCELLATION("reservation.removed.because.of.cancellation"),
    ORDER_COMPLETED("order.completed"),
    ORDER_SELLING_STARTED("order.selling.started"),
    ORDER_CANCELLING_STARTED("order.cancelling.started"),
    ORDER_SOLD("order.sold"),
    ORDER_CANCELLED("order.cancelled");

    override fun toString(): String = this.value
}
