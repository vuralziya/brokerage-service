package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.CompleteCancellation
import org.springframework.stereotype.Component

data class ReservationRemovedBecauseOfCancellation(
    val orderId: String
) : Notification

@Component
class ReservationRemovedBecauseOfCancellationAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<ReservationRemovedBecauseOfCancellation> {

    override suspend fun handle(notification: ReservationRemovedBecauseOfCancellation) = with(notification) {
        commandBus.executeCommandAsync(CompleteCancellation(orderId))
    }
}
