package com.zv.brokerageservice.application.command

import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.order.OrderId
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.user.UserRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AddOrderToUserAsyncHandlerTest {

    @InjectMocks
    private lateinit var addOrderToUserAsyncHandler: AddOrderToUserAsyncHandler

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun it_should_throw_exception_when_order_not_found() = runBlockingTest {
        //given
        val orderId = "testOrderId"
        given(orderRepository.load(orderId)).willReturn(null)
        val command = AddOrderToUser(orderId)

        //when
        val throwable = catchThrowable { runBlocking { addOrderToUserAsyncHandler.handleAsync(command) } }

        //then
        assertThat(throwable.message).isEqualTo("order.must.not.be.null")
    }

    @Test
    fun it_should_throw_exception_when_user_not_found() = runBlockingTest {
        //given
        val userId = "testUserId"
        val orderId = "testOrderId"
        given(orderRepository.load(orderId)).willReturn(Order.create(OrderId.create(), userId, "test", 1))
        given(userRepository.load(userId)).willReturn(null)
        val command = AddOrderToUser(orderId)

        //when
        val throwable = catchThrowable { runBlocking { addOrderToUserAsyncHandler.handleAsync(command) } }

        //then
        assertThat(throwable.message).isEqualTo("user.must.not.be.null")
    }
}