package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.ReactiveCluster
import com.couchbase.client.java.ReactiveCollection
import com.zv.brokerageservice.domain.stock.Stock
import com.zv.brokerageservice.domain.stock.StockRepository
import org.springframework.stereotype.Repository

@Repository
class CouchbaseStockRepository(
    cluster: ReactiveCluster,
    collection: ReactiveCollection
) : BaseRepositoryImpl<String, Stock>(cluster, collection, Stock::class.java), StockRepository