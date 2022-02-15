package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.Notifier
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.user.UserRepository
import org.springframework.stereotype.Component

data class NotifyUser(val orderId: String, val subject: String, val text: String) : Command

@Component
class NotifyUserAsyncHandler(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val notifier: Notifier
) :
    AsyncCommandHandler<NotifyUser> {

    override suspend fun handleAsync(command: NotifyUser) = with(command) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        val user = userRepository.load(order.userId) ?: throw NotFoundException("user.must.not.be.null")
        notifier.sendSimpleMessage(user.email, subject, "$text $orderId")
    }
}