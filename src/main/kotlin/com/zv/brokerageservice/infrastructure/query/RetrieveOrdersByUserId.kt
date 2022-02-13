package com.zv.brokerageservice.infrastructure.query

import com.trendyol.kediatr.AsyncQueryHandler
import com.trendyol.kediatr.Query
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.user.UserRepository
import com.zv.brokerageservice.infrastructure.api.response.OrderDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

data class RetrieveOrdersByUserId(val userId: String) : Query<Collection<OrderDto>>


@Component
class RetrieveOrdersByUserIdAsyncHandler(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) :
    AsyncQueryHandler<RetrieveOrdersByUserId, Collection<OrderDto>> {

    override suspend fun handleAsync(query: RetrieveOrdersByUserId): Collection<OrderDto> = coroutineScope {
        with(query) {
            val user = userRepository.load(userId) ?: throw NotFoundException("user.not.found", userId)
            val orders = user.orders.map {
                async { orderRepository.load(it) }
            }.awaitAll().filterNotNull()
            return@with orders.map { OrderDto(it.item) }
        }
    }
}


