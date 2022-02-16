package com.zv.brokerageservice.application.notification

import com.trendyol.kediatr.AsyncNotificationHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Notification
import com.zv.brokerageservice.application.command.RemoveReservationsForOrder
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.domain.stock.Reason
import org.springframework.stereotype.Component

data class OrderSellingStarted(val orderId: String) : Notification

@Component
class OrderSellingStartedAsyncHandler(
    private val commandBus: CommandBus,
    private val orderRepository: OrderRepository
) :
    AsyncNotificationHandler<OrderSellingStarted> {

    override suspend fun handle(notification: OrderSellingStarted) = with(notification) {
        val order = orderRepository.load(orderId) ?: throw NotFoundException("order.must.not.be.null")
        commandBus.executeCommandAsync(RemoveReservationsForOrder(order, Reason.SELL))
    }
}
