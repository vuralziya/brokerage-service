package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.NotifyUser
import org.springframework.stereotype.Component

data class OrderSold(val orderId: String) : Notification

@Component
class OrderSoldAsyncHandler(private val commandBus: CommandBus) : AsyncNotificationHandler<OrderSold> {

    override suspend fun handle(notification: OrderSold) = with(notification) {
        commandBus.executeCommandAsync(NotifyUser(orderId, "Order Sold", "Order sold successfully"))
    }
}
