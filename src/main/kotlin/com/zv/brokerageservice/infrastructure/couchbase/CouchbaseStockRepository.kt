package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.ReactiveCluster
import com.couchbase.client.java.ReactiveCollection
import com.couchbase.client.java.json.JsonObject
import com.couchbase.client.java.kv.GetResult
import com.zv.brokerageservice.domain.stock.Reservation
import com.zv.brokerageservice.domain.stock.Stock
import com.zv.brokerageservice.domain.stock.StockRepository
import com.zv.brokerageservice.infrastructure.messaging.KafkaDomainEventPublisher
import org.springframework.stereotype.Repository

@Repository
class CouchbaseStockRepository(
    cluster: ReactiveCluster,
    collection: ReactiveCollection,
    kafkaDomainEventPublisher: KafkaDomainEventPublisher
) : BaseRepositoryImpl<String, Stock>(cluster, collection, kafkaDomainEventPublisher, Stock::class.java),
    StockRepository {

    override suspend fun map(getResult: GetResult): Stock {
        val jsonObject = getResult.contentAsObject()
        val reservations = getReservations(jsonObject)
        val stock = Stock.from(
            jsonObject.getString("id"),
            jsonObject.getString("productName"),
            jsonObject.getInt("quantity"),
            reservations
        )
        stock.cas = getResult.cas()
        return stock
    }

    private suspend fun getReservations(jsonObject: JsonObject): List<Reservation> {
        val reservations = mutableSetOf<Reservation>()
        val jsonArrayReservations = jsonObject.getArray("reservations")
        for (i in 0 until jsonArrayReservations.size()) {
            val jsonReservation = jsonArrayReservations.getObject(i)
            val reservation = Reservation(
                jsonReservation.getString("orderId"),
                jsonReservation.getInt("quantity")
            )
            reservations.add(reservation)
        }
        return reservations.toList()
    }
}