package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Component

data class CompleteOrder(val orderId: String) : Command

@Component
class CompleteOrderAsyncHandler(private val orderRepository: OrderRepository) : AsyncCommandHandler<CompleteOrder> {

    override suspend fun handleAsync(command: CompleteOrder) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        order.complete()
        orderRepository.save(order)
    }
}