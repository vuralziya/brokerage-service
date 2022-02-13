package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.ReactiveCluster
import com.couchbase.client.java.ReactiveCollection
import com.zv.brokerageservice.domain.user.User
import com.zv.brokerageservice.domain.user.UserId
import com.zv.brokerageservice.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class CouchbaseUserRepository(
    cluster: ReactiveCluster,
    collection: ReactiveCollection
) : BaseRepositoryImpl<UserId, User>(cluster, collection, User::class.java), UserRepository