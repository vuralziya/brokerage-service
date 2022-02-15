package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.NotifyUser
import org.springframework.stereotype.Component

data class OrderCompleted(val orderId: String) : Notification

@Component
class OrderCompletedAsyncHandler(private val commandBus: CommandBus) : AsyncNotificationHandler<OrderCompleted> {

    override suspend fun handle(notification: OrderCompleted) = with(notification) {
        commandBus.executeCommandAsync(NotifyUser(orderId, "Order Completed", "Order completed successfully"))
    }
}
