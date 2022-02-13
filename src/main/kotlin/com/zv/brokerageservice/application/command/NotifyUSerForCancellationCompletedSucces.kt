package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import org.springframework.stereotype.Component

data class NotifyUSerForCancellationCompletedSucces(val orderId: String) : Command

@Component
class NotifyUSerForCancellationCompletedSuccesAsyncHandler :
    AsyncCommandHandler<NotifyUSerForCancellationCompletedSucces> {

    override suspend fun handleAsync(command: NotifyUSerForCancellationCompletedSucces) {
        TODO("notify user")
    }
}



