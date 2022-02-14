package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.ReactiveCluster
import com.couchbase.client.java.ReactiveCollection
import com.couchbase.client.java.kv.GetResult
import com.zv.brokerageservice.domain.user.User
import com.zv.brokerageservice.domain.user.UserId
import com.zv.brokerageservice.domain.user.UserRepository
import com.zv.brokerageservice.infrastructure.messaging.KafkaDomainEventPublisher
import org.springframework.stereotype.Repository

@Repository
class CouchbaseUserRepository(
    cluster: ReactiveCluster,
    collection: ReactiveCollection,
    kafkaDomainEventPublisher: KafkaDomainEventPublisher
) : BaseRepositoryImpl<UserId, User>(cluster, collection, kafkaDomainEventPublisher, User::class.java), UserRepository {
    override suspend fun map(getResult: GetResult): User {
        val jsonObject = getResult.contentAsObject()
        return User.from(
            jsonObject.getString("id"),
            jsonObject.getString("name"),
            jsonObject.getString("lastName"),
            jsonObject.getLong("identityNumber"),
            jsonObject.getString("citizen"),
            jsonObject.getArray("orders").map { it.toString() }.toList()
        )
    }
}