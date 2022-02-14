package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.order.OrderId
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.user.UserRepository
import org.springframework.stereotype.Component

data class BuyOrder(
    val userId: String,
    val stockId: String,
    val quantity: Int
) : Command

@Component
class BuyOrderAsyncHandler(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : AsyncCommandHandler<BuyOrder> {

    override suspend fun handleAsync(command: BuyOrder) = with(command) {
        userRepository.load(userId) ?: throw NotFoundException("user.not.found", userId)
        val orderId = OrderId.create()
        val order = Order.create(orderId, userId, stockId, quantity)
        orderRepository.save(order)
    }
}
