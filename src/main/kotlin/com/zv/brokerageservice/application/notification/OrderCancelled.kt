package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.NotifyUSerForCancellationCompletedSucces
import org.springframework.stereotype.Component

data class OrderCancelled(val orderId: String) : Notification

@Component
class OrderCancelledAsyncHandler(private val commandBus: CommandBus) : AsyncNotificationHandler<OrderCancelled> {

    override suspend fun handle(notification: OrderCancelled) = with(notification) {
        commandBus.executeCommandAsync(NotifyUSerForCancellationCompletedSucces(orderId))
    }
}