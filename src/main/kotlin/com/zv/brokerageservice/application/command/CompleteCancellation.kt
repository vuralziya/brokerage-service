package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Component

data class CompleteCancellation(val orderId: String) : Command

@Component
class CompleteCancellationAsyncHandler(private val orderRepository: OrderRepository) :
    AsyncCommandHandler<CompleteCancellation> {

    override suspend fun handleAsync(command: CompleteCancellation) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        order.completeCancellation()
        orderRepository.save(order)
    }
}
