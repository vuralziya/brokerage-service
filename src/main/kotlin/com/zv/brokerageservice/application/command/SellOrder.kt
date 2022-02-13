package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Component

data class SellOrder(val orderId: String) : Command

@Component
class SellOrderAsyncHandler(private val orderRepository: OrderRepository) : AsyncCommandHandler<SellOrder> {

    override suspend fun handleAsync(command: SellOrder) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        order.sell()
        orderRepository.save(order)
    }
}