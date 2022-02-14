package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.user.UserRepository
import org.springframework.stereotype.Component

data class AddOrderToUser(val orderId: String) : Command

@Component
class AddOrderToUserAsyncHandler(
    private val orderRepository: OrderRepository,
    private val useRepository: UserRepository
) : AsyncCommandHandler<AddOrderToUser> {

    override suspend fun handleAsync(command: AddOrderToUser) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null", orderId)
        val userId = order.userId
        val user = useRepository.load(userId) ?: throw NotFoundException("user.must.not.be.null", userId)
        user.addOrder(orderId)
        useRepository.save(user)
    }
}
