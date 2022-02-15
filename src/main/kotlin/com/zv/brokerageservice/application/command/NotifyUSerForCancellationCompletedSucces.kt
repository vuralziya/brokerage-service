package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import org.springframework.stereotype.Component

data class NotifyUSerForCancellationCompletedSuccess(val orderId: String) : Command

@Component
class NotifyUSerForCancellationCompletedSuccessAsyncHandler :
    AsyncCommandHandler<NotifyUSerForCancellationCompletedSuccess> {

    override suspend fun handleAsync(command: NotifyUSerForCancellationCompletedSuccess) {

    }
}



