package com.zv.brokerageservice.domain.stock

import com.zv.brokerageservice.domain.event.EventType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class StockTest {

    @Test
    fun it_should_create() {
        //when
        val stockId = StockId("test")
        val stock = Stock.create(stockId, "APPLE", 10)

        //then
        Assertions.assertThat(stock.domainEvents().size).isEqualTo(1)
        Assertions.assertThat(stock.domainEvents().first().type).isEqualTo(EventType.STOCK_CREATED)
        Assertions.assertThat(stock.id).isEqualTo(stockId.value)

    }

    @Test
    fun it_should_throw_insufficient_when_available_quantity_lte_requested_quantity() {
        //given
        val requestedQuantity = 100
        val stock = Stock.create(StockId("test"), "APPLE", 10)
        val orderId = "testOrderId"

        //when
        val throwable = Assertions.catchThrowable {
            stock.makeReservation(orderId, requestedQuantity)
        }

        //then
        Assertions.assertThat(throwable.message).isEqualTo("insufficient.stock.for.order")
    }

    @Test
    fun it_should_register_reservation_removed_because_of_selling_when_reason_is_sell() {
        //given
        val stock = Stock.create(StockId("test"), "APPLE", 10)
        val orderId1 = "testOrderId1"
        val orderId2 = "testOrderId2"
        stock.makeReservation(orderId1, 3)
        stock.makeReservation(orderId2, 5)

        //when
        stock.removeReservation(orderId1, Reason.SELL)

        //then
        Assertions.assertThat(stock.domainEvents().map { it.type }
            .contains(EventType.RESERVATION_REMOVED_BECAUSE_OF_SELLING)).isTrue
    }

    @Test
    fun it_should_register_reservation_removed_because_of_cancellation_when_reason_is_cancell() {
        //given
        val stock = Stock.create(StockId("test"), "APPLE", 10)
        val orderId1 = "testOrderId1"
        val orderId2 = "testOrderId2"
        stock.makeReservation(orderId1, 3)
        stock.makeReservation(orderId2, 5)

        //when
        stock.removeReservation(orderId1, Reason.CANCEL)

        //then
        Assertions.assertThat(stock.domainEvents().map { it.type }
            .contains(EventType.RESERVATION_REMOVED_BECAUSE_OF_CANCELLATION)).isTrue
    }
}