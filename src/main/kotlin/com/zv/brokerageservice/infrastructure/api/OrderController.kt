package com.zv.brokerageservice.infrastructure.api

import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.domain.event.BuyOrderRequestReceived
import com.zv.brokerageservice.domain.event.CancelOrderRequestReceived
import com.zv.brokerageservice.domain.event.SellOrderRequestReceived
import com.zv.brokerageservice.infrastructure.api.request.BuyOrderRequest
import com.zv.brokerageservice.infrastructure.api.request.CancelOrderRequest
import com.zv.brokerageservice.infrastructure.api.request.SellOrderRequest
import com.zv.brokerageservice.infrastructure.api.response.OrderDto
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaDomainEventPublisher
import com.zv.brokerageservice.infrastructure.query.RetrieveOrder
import com.zv.brokerageservice.infrastructure.query.RetrieveOrdersByUserId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val commandBus: CommandBus,
    private val kafkaDomainEventPublisher: KafkaDomainEventPublisher
) {

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun buyOrder(
        @RequestBody request: BuyOrderRequest
    ) = with(request) {
        kafkaDomainEventPublisher.publish(BuyOrderRequestReceived(userId, stockId, quantity))
    }

    @PostMapping("/sell")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun sellOrder(
        @RequestBody request: SellOrderRequest
    ) = with(request) {
        kafkaDomainEventPublisher.publish(SellOrderRequestReceived(orderId))
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun cancelOrder(
        @RequestBody request: CancelOrderRequest
    ) = with(request) {
        kafkaDomainEventPublisher.publish(CancelOrderRequestReceived(orderId))
    }

    @GetMapping("/{orderId}")
    suspend fun retrieveOrder(
        @PathVariable("orderId") orderId: String
    ): ResponseEntity<OrderDto> {
        val orderDto = commandBus.executeQueryAsync(RetrieveOrder(orderId))
        return ResponseEntity<OrderDto>(orderDto, HttpStatus.OK)
    }

    @GetMapping("/users/{userId}")
    suspend fun retrieveOrdersByUserId(
        @PathVariable("userId") userId: String
    ): ResponseEntity<Collection<OrderDto>> {
        val orderDto = commandBus.executeQueryAsync(RetrieveOrdersByUserId(userId))
        return ResponseEntity<Collection<OrderDto>>(orderDto, HttpStatus.OK)
    }
}