package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.NotifyUser
import org.springframework.stereotype.Component

data class OrderCancelled(val orderId: String) : Notification

@Component
class OrderCancelledAsyncHandler(private val commandBus: CommandBus) : AsyncNotificationHandler<OrderCancelled> {

    override suspend fun handle(notification: OrderCancelled) = with(notification) {
        commandBus.executeCommandAsync(NotifyUser(orderId, "Order Cancelled", "Order cancelled successfully"))
    }
}