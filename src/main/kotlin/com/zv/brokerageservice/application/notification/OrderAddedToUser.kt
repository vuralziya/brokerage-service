package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.MakeReservationForOrder
import org.springframework.stereotype.Component

data class OrderAddedToUser(
    val userId: String,
    val orderId: String
) : Notification

@Component
class OrderAddedToUserAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<OrderAddedToUser> {

    override suspend fun handle(notification: OrderAddedToUser) = with(notification) {
        commandBus.executeCommandAsync(MakeReservationForOrder(orderId))
    }
}