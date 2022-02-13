package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.SellOrder
import org.springframework.stereotype.Component

data class SellOrderRequestReceived(
    val orderId: String
) : Notification

@Component
class SellOrderRequestReceivedAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<SellOrderRequestReceived> {

    override suspend fun handle(notification: SellOrderRequestReceived) = with(notification) {
        commandBus.executeCommandAsync(SellOrder(orderId))
    }
}