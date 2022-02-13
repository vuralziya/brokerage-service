package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.order.OrderId
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Component

data class BuyOrder(
    val userId: String,
    val stockId: String,
    val quantity: Int
) : Command

@Component
class BuyOrderAsyncHandler(
    private val orderRepository: OrderRepository
) : AsyncCommandHandler<BuyOrder> {

    override suspend fun handleAsync(command: BuyOrder) = with(command) {
        val orderId = OrderId.create()
        val order = Order.create(orderId, userId, stockId, quantity)
        orderRepository.save(order)
    }
}
