package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Component

data class CompleteOrderSelling(
    val orderId: String
) : Command

@Component
class CompleteOrderSellingAsyncHandler(private val orderRepository: OrderRepository) :
    AsyncCommandHandler<CompleteOrderSelling> {

    override suspend fun handleAsync(command: CompleteOrderSelling) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        order.completeSelling()
        orderRepository.save(order)
    }
}