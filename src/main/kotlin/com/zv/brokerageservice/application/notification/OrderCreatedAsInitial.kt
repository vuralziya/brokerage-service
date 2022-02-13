package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.AddOrderToUser
import org.springframework.stereotype.Component

data class OrderCreatedAsInitial(val orderId: String) : Notification

@Component
class OrderCreatedAsInitialAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<OrderCreatedAsInitial> {

    override suspend fun handle(notification: OrderCreatedAsInitial) = with(notification) {
        commandBus.executeCommandAsync(AddOrderToUser(orderId))
    }
}