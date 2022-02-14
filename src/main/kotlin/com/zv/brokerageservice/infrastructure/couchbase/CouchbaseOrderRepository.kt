package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.ReactiveCluster
import com.couchbase.client.java.ReactiveCollection
import com.couchbase.client.java.kv.GetResult
import com.zv.brokerageservice.domain.order.Item
import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.order.OrderId
import com.zv.brokerageservice.domain.order.OrderRepository
import com.zv.brokerageservice.infrastructure.messaging.KafkaDomainEventPublisher
import org.springframework.stereotype.Repository

@Repository
class CouchbaseOrderRepository(
    cluster: ReactiveCluster,
    collection: ReactiveCollection,
    kafkaDomainEventPublisher: KafkaDomainEventPublisher
) : BaseRepositoryImpl<OrderId, Order>(cluster, collection, kafkaDomainEventPublisher, Order::class.java),
    OrderRepository {
    override suspend fun map(getResult: GetResult): Order {
        val jsonObject = getResult.contentAsObject()
        val itemObject = jsonObject.getObject("item")
        val item = Item(jsonObject.getString("stockId"), jsonObject.getInt("quantity"))
        return Order.from(
            jsonObject.getString("id"),
            jsonObject.getString("userId"),
            item,
            jsonObject.getString("status")
        )
    }
}