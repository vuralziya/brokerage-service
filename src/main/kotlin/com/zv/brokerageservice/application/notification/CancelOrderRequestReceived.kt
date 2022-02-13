package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.BuyOrder
import com.zv.brokerageservice.application.command.CancelOrder
import org.springframework.stereotype.Component

data class CancelOrderRequestReceived(
    val orderId: String
) : Notification

@Component
class CancelOrderRequestReceivedAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<CancelOrderRequestReceived> {

    override suspend fun handle(notification: CancelOrderRequestReceived) = with(notification) {
        val command = CancelOrder(orderId)
        commandBus.executeCommandAsync(command)
    }
}