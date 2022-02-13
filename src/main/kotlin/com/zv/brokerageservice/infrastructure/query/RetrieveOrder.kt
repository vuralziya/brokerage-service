package com.zv.brokerageservice.infrastructure.query

import com.trendyol.kediatr.AsyncQueryHandler
import com.trendyol.kediatr.Query
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.infrastructure.api.response.OrderDto
import org.springframework.stereotype.Component

data class RetrieveOrder(val orderId: String) : Query<OrderDto>

@Component
class RetrieveOrderAsyncHandler(private val orderRepository: OrderRepository) :
    AsyncQueryHandler<RetrieveOrder, OrderDto> {

    override suspend fun handleAsync(query: RetrieveOrder): OrderDto = with(query) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        return@with OrderDto(order.item)
    }
}