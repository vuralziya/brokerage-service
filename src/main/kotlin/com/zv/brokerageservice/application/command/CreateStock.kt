package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.domain.stock.Stock
import com.zv.brokerageservice.domain.stock.StockId
import com.zv.brokerageservice.domain.stock.StockRepository
import org.springframework.stereotype.Component

data class CreateStock(val productName: String, val quantity: Int) : Command

@Component
class CreateStockAsyncHandler(private val stockRepository: StockRepository) : AsyncCommandHandler<CreateStock> {

    override suspend fun handleAsync(command: CreateStock) = with(command) {
        val stockId = StockId.create()
        val stock = Stock.create(stockId, productName, quantity)
        stockRepository.save(stock)
    }
}
