package com.zv.brokerageservice.infrastructure.api

import com.trendyol.kediatr.CommandBus
import com.zv.brokerageservice.domain.event.BuyOrderRequestReceived
import com.zv.brokerageservice.domain.event.CancelOrderRequestReceived
import com.zv.brokerageservice.domain.event.SellOrderRequestReceived
import com.zv.brokerageservice.domain.order.Item
import com.zv.brokerageservice.infrastructure.api.request.BuyOrderRequest
import com.zv.brokerageservice.infrastructure.api.request.CancelOrderRequest
import com.zv.brokerageservice.infrastructure.api.request.SellOrderRequest
import com.zv.brokerageservice.infrastructure.api.response.OrderDto
import com.zv.brokerageservice.infrastructure.messaging.kafka.KafkaDomainEventPublisher
import com.zv.brokerageservice.infrastructure.query.RetrieveOrder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(OrderController::class)
internal class OrderControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var commandBus: CommandBus

    @MockBean
    lateinit var kafkaDomainEventPublisher: KafkaDomainEventPublisher

    @Test
    fun buyShouldPublishEvent() = runBlocking {
        val buyOrderRequest = BuyOrderRequest("test", "stockId", 1)

        webTestClient
            .post()
            .uri("/orders/buy")
            .bodyValue(buyOrderRequest)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isAccepted

        BDDMockito.verify(kafkaDomainEventPublisher).publish(
            BuyOrderRequestReceived(
                buyOrderRequest.userId,
                buyOrderRequest.stockId,
                buyOrderRequest.quantity
            )
        )
    }

    @Test
    fun sellShouldPublishEvent() = runBlocking {
        val sellOrderRequest = SellOrderRequest("testOrderId")

        webTestClient
            .post()
            .uri("/orders/sell")
            .bodyValue(sellOrderRequest)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isAccepted

        BDDMockito.verify(kafkaDomainEventPublisher).publish(
            SellOrderRequestReceived(
                sellOrderRequest.orderId
            )
        )
    }

    @Test
    fun cancelShouldPublishEvent() = runBlocking {
        val cancelOrderRequest = CancelOrderRequest("testOrderId")

        webTestClient
            .post()
            .uri("/orders/cancel")
            .bodyValue(cancelOrderRequest)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isAccepted

        BDDMockito.verify(kafkaDomainEventPublisher).publish(
            CancelOrderRequestReceived(
                cancelOrderRequest.orderId
            )
        )
    }

    @Test
    fun retrieveShouldGetOrder() = runBlockingTest {
        val givenOrderId = "123test"
        val order = OrderDto(Item("test", 2))

        Mockito.`when`(
            commandBus.executeQueryAsync(
                RetrieveOrder(
                    givenOrderId
                )
            )
        ).thenReturn(order)

        webTestClient
            .get()
            .uri {
                it.path("/orders/$givenOrderId")
                it.build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk

        BDDMockito.verify(commandBus).executeQueryAsync(
            RetrieveOrder(
                givenOrderId
            )
        )
    }
}