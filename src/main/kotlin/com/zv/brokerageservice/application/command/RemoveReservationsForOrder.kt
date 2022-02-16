package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.stock.Reason
import com.zv.brokerageservice.domain.stock.StockRepository
import org.springframework.stereotype.Component

data class RemoveReservationsForOrder(val order: Order, val reason: Reason) : Command

@Component
class RemoveReservationsForOrderAsyncHandler(
    private val stockRepository: StockRepository
) : AsyncCommandHandler<RemoveReservationsForOrder> {

    override suspend fun handleAsync(command: RemoveReservationsForOrder) = with(command) {
        val item = order.item
        val stockId = item.stockId
        val stock = stockRepository.load(stockId) ?: throw NotFoundException("stock.must.not.be.null")
        stock.removeReservation(order.id, reason)
        stockRepository.save(stock)
    }
}

