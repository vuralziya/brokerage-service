package com.zv.brokerageservice.application.command

import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.Command
import com.zv.brokerageservice.application.exception.ApplicationException
import com.zv.brokerageservice.domain.user.User
import com.zv.brokerageservice.domain.user.UserId
import com.zv.brokerageservice.domain.user.UserRepository
import org.springframework.stereotype.Component

data class CreateUser(
    val firstName: String,
    val lastName: String,
    val identityNumber: Long,
    val citizen: String
) : Command

@Component
class CreateUserAsyncHandler(private val userRepository: UserRepository) : AsyncCommandHandler<CreateUser> {

    override suspend fun handleAsync(command: CreateUser) = with(command) {
        val userId = UserId.create(identityNumber, citizen)
        val user = userRepository.load(userId.value)
        if (user != null) {
            throw ApplicationException("user.already.exists", userId.value)
        }
        val newUser = User.create(firstName, lastName, identityNumber, citizen)
        userRepository.save(newUser)
    }
}
