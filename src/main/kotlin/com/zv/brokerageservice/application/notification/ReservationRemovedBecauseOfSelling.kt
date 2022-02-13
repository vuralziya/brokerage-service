package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.CompleteOrderSelling
import org.springframework.stereotype.Component

data class ReservationRemovedBecauseOfSelling(
    val orderId: String
) : Notification

@Component
class ReservationRemovedBecauseOfSellingAsyncHandler(private val commandBus: CommandBus) :
    AsyncNotificationHandler<ReservationRemovedBecauseOfSelling> {

    override suspend fun handle(notification: ReservationRemovedBecauseOfSelling) = with(notification) {
        commandBus.executeCommandAsync(CompleteOrderSelling(orderId))
    }
}
