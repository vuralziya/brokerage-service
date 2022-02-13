package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import org.springframework.stereotype.Component

data class NotifyUserForSellOrderCompleted(val orderId: String) : Command

@Component
class NotifyUserForSellOrderCompletedAsyncHandler : AsyncCommandHandler<NotifyUserForSellOrderCompleted> {

    override suspend fun handleAsync(command: NotifyUserForSellOrderCompleted) {
        TODO("notify user")
    }
}
