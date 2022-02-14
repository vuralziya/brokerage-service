package com.zv.brokerageservice.domain.stock

import com.zv.brokerageservice.domain.AggregateRoot
import com.zv.brokerageservice.domain.DomainException
import com.zv.brokerageservice.domain.event.ReservationCompleted
import com.zv.brokerageservice.domain.event.ReservationRemovedBecauseOfCancellation
import com.zv.brokerageservice.domain.event.ReservationRemovedBecauseOfSelling
import com.zv.brokerageservice.domain.event.StockCreated

class Stock private constructor(
    id: String,
    val productName: String,
    val quantity: Int
) : AggregateRoot<Stock>(id) {

    override val type: String
        get() = TYPE

    companion object {
        const val TYPE = "Stock"
        fun create(id: StockId, productName: String, quantity: Int): Stock {
            val stock = Stock(id.value, productName, quantity)
            stock.registerEvent(StockCreated(stock.id))
            return stock
        }

        fun from(id: String, productName: String, quantity: Int, reservations: List<Reservation>): Stock {
            val stock = Stock(id, productName, quantity)
            stock.reservations = reservations.toMutableList()
            return stock
        }
    }

    var reservations = mutableListOf<Reservation>()
        private set

    private val availableQuantity: Int
        get() {
            return (quantity - reservations.sumOf { it.quantity }).coerceAtLeast(0)
        }

    fun makeReservation(orderId: String, quantity: Int) {
        if (availableQuantity < quantity) {
            throw DomainException("insufficient.stock.for.order", orderId)
        }
        reservations.add(Reservation(orderId, quantity))
        registerEvent(ReservationCompleted(orderId))
    }

    fun removeReservation(orderId: String, reason: Reason) {
        val reservation = reservations.firstOrNull { it.orderId == orderId }
        reservations.remove(reservation)
        if (Reason.SELL == reason) {
            registerEvent(ReservationRemovedBecauseOfSelling(orderId))
        }
        if (Reason.CANCEL == reason) {
            registerEvent(ReservationRemovedBecauseOfCancellation(orderId))
        }
    }
}

data class Reservation(val orderId: String, val quantity: Int)

enum class Reason {
    CANCEL, SELL
}