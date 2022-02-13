package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import org.springframework.stereotype.Component

data class NotifyUserForSuccessOrder(val orderId: String) : Command

@Component
class NotifyUserForSuccessOrderAsyncHandler : AsyncCommandHandler<NotifyUserForSuccessOrder> {

    override suspend fun handleAsync(command: NotifyUserForSuccessOrder) {
        TODO("notify user with notification, mail or message")
    }
}