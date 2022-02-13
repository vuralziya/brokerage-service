package com.zv.brokerageservice.infrastructure.query

import com.trendyol.kediatr.AsyncQueryHandler
import com.trendyol.kediatr.Query
import com.zv.brokerageservice.application.exception.NotFoundException
import com.zv.brokerageservice.domain.user.UserRepository
import com.zv.brokerageservice.infrastructure.api.response.UserDto
import org.springframework.stereotype.Component

data class RetrieveUser(
    val id: String
) : Query<UserDto>

@Component
class RetrieveUserAsyncHandler(private val userRepository: UserRepository) : AsyncQueryHandler<RetrieveUser, UserDto> {

    override suspend fun handleAsync(query: RetrieveUser): UserDto = with(query) {
        val user = userRepository.load(id) ?: throw NotFoundException("user.not.found.by.id", id)
        return@with UserDto(
            user.name,
            user.lastName,
            user.identityNumber,
            user.citizen
        )
    }
}
