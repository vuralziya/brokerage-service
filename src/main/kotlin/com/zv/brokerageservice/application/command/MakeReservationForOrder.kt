package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.stock.StockRepository
import org.springframework.stereotype.Component

data class MakeReservationForOrder(val orderId: String) : Command

@Component
class MakeReservationForOrderAsyncHandler(
    private val orderRepository: OrderRepository,
    private val stockRepository: StockRepository
) :
    AsyncCommandHandler<MakeReservationForOrder> {

    override suspend fun handleAsync(command: MakeReservationForOrder) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException(
            "order.not.found",
            orderId
        ) // TODO do not forget listen exception
        val item = order.item
        val stockId = item.stockId
        val stock = stockRepository.load(stockId) ?: throw NotFoundException("stock.not.found.for.order", stockId)
        stock.makeReservation(orderId, item.quantity)
        stockRepository.save(stock)
    }
}
