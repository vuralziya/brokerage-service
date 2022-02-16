package com.zv.brokerageservice.application.command

import com.zv.brokerageservice.argumentCaptor
import com.zv.brokerageservice.capture
import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.user.User
import com.zv.brokerageservice.domain.user.UserRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class BuyOrderAsyncHandlerTest {
    @InjectMocks
    private lateinit var buyOrderAsyncHandler: BuyOrderAsyncHandler

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository


    @Test
    fun it_should_throw_exception_when_user_not_found() = runBlockingTest {
        //given
        val userId = "testUserId"
        BDDMockito.given(userRepository.load(userId)).willReturn(null)
        val command = BuyOrder(userId, "testStock", 1)

        //when
        val throwable = Assertions.catchThrowable { runBlocking { buyOrderAsyncHandler.handleAsync(command) } }

        //then
        Assertions.assertThat(throwable.message).isEqualTo("user.not.found")
    }

    @Test
    fun it_should_save_order() = runBlockingTest {
        //given
        val userId = "TR_123"
        val user = User.create("zi", "vu", 123, "test", "TR")
        BDDMockito.given(userRepository.load(userId)).willReturn(user)
        val command = BuyOrder(userId, "testStock", 1)

        //when
        buyOrderAsyncHandler.handleAsync(command)

        //then
        val captor = argumentCaptor<Order>()
        verify(orderRepository, times(1)).save(capture(captor))
        assertTrue(captor.value.userId == userId)

    }
}