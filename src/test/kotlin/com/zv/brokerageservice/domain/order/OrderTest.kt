package com.zv.brokerageservice.domain.order

import com.zv.brokerageservice.domain.event.EventType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class OrderTest {

    @Test
    fun it_should_create_object() {
        //given
        val orderId = OrderId.create()
        val userId = "test"
        val stockId = "testStock"
        val quantity = 1

        //when
        val order = Order.create(orderId, userId, stockId, quantity)

        //then
        assertThat(order.domainEvents().size).isEqualTo(1)
        assertThat(order.domainEvents().first().type).isEqualTo(EventType.ORDER_CREATED_AS_INITIAL)
        assertThat(order.id).isEqualTo(orderId.value)
    }

    @Test
    fun it_should_complete_order() {
        //given
        val order = createTestOrder("INITIAL")

        //when
        order.complete()

        //then
        assertThat(order.domainEvents().size).isEqualTo(2)
        assertThat(order.domainEvents().map { it.type }).contains(EventType.ORDER_COMPLETED)
    }

    @Test
    fun it_should_return_if_status_already_completed() {
        //given
        val order = createTestOrder("COMPLETED")

        //when
        order.complete()

        //then
        assertThat(order.domainEvents().size).isEqualTo(1)
    }

    @Test
    fun it_should_throw_exception_when_order_status_is_not_initial() {
        //given
        val order = createTestOrder("SELLING")

        //when
        val throwable = catchThrowable { order.complete() }

        //then
        assertThat(throwable.message).isEqualTo("oder.status.should.be.initial.in.order.to.complete")
    }

    @Test
    fun it_should_throw_exception_when_order_status_is_not_completed() {
        //given
        val order = createTestOrder("INITIAL")

        //when
        val throwable = catchThrowable { order.sell() }

        //then
        assertThat(throwable.message).isEqualTo("not.completed.orders.can.not.be.sell")
    }

    @Test
    fun it_should_throw_exception_when_order_status_is_not_selling() {
        //given
        val order = createTestOrder("COMPLETED")

        //when
        val throwable = catchThrowable { order.completeSelling() }

        //then
        assertThat(throwable.message).isEqualTo("oder.status.should.be.selling.in.order.to.sell")
    }

    private fun createTestOrder(status: String): Order {
        val orderId = OrderId.create()
        val userId = "test"
        val stockId = "testStock"
        val quantity = 1
        val order = Order.create(orderId, userId, stockId, quantity)
        order.status = OrderStatus.valueOf(status)
        return order
    }
}