package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.BuyOrder
import org.springframework.stereotype.Component

data class BuyOrderRequestReceived(
    val userId: String,
    val stockId: String,
    val quantity: Int
) : Notification

@Component
class BuyOrderRequestReceivedAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<BuyOrderRequestReceived> {

    override suspend fun handle(notification: BuyOrderRequestReceived) = with(notification) {
        val command = BuyOrder(userId, stockId, quantity)
        commandBus.executeCommandAsync(command)
    }
}