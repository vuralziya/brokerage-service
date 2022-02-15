package com.zv.brokerageservice.infrastructure.api

import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.application.command.CreateUser
import com.zv.brokerageservice.infrastructure.api.request.CreateUserRequest
import com.zv.brokerageservice.infrastructure.api.response.UserDto
import com.zv.brokerageservice.infrastructure.query.RetrieveUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("user-controller")
@RequestMapping("/users")
class UserController(private val commandBus: CommandBus) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(
        @RequestBody request: CreateUserRequest
    ) {
        commandBus.executeCommandAsync(
            CreateUser(
                request.firstName,
                request.lastName,
                request.identityNumber,
                request.email,
                request.citizen
            )
        )
    }

    suspend fun get(
        @PathVariable("userId") userId: String
    ): ResponseEntity<UserDto> {
        val userDto = commandBus.executeQueryAsync(RetrieveUser(userId))
        return ResponseEntity<UserDto>(userDto, HttpStatus.OK)
    }
}