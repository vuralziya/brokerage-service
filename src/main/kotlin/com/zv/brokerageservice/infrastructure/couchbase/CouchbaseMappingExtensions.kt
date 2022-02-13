package com.zv.brokerageservice.infrastructure.couchbase

import com.couchbase.client.java.kv.GetResult
import com.zv.brokerageservice.domain.AggregateRoot

inline fun <reified T : AggregateRoot<T>> GetResult.initializeAggregate(): T = initializeAggregate(T::class.java)

fun <T : AggregateRoot<T>> GetResult.initializeAggregate(clazz: Class<T>): T {
    val aggregate = this.contentAs(clazz)
    aggregate.cas = this.cas()
    aggregate.clearDomainEvents()
    return aggregate
}

