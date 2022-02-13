package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.ReactiveCluster
import com.couchbase.client.java.ReactiveCollection
import com.zv.brokerageservice.domain.order.Order
import com.zv.brokerageservice.domain.order.OrderId
import com.zv.brokerageservice.domain.order.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class CouchbaseOrderRepository(
    cluster: ReactiveCluster,
    collection: ReactiveCollection
) : BaseRepositoryImpl<OrderId, Order>(cluster, collection, Order::class.java), OrderRepository