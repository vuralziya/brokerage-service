package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Component

data class CancelOrder(val orderId: String) : Command

@Component
class CancelOrderAsyncHandler(private val orderRepository: OrderRepository) : AsyncCommandHandler<CancelOrder> {

    override suspend fun handleAsync(command: CancelOrder) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        order.cancel()
        orderRepository.save(order)
    }
}


