package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.CompleteOrder
import org.springframework.stereotype.Component

data class ReservationCompleted(
    val orderId: String
) : Notification

@Component
class ReservationCompletedAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<ReservationCompleted> {

    override suspend fun handle(notification: ReservationCompleted) = with(notification) {
        commandBus.executeCommandAsync(CompleteOrder(orderId))
    }
}
