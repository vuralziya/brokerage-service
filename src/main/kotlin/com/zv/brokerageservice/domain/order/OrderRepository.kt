package com.zv.brokerageservice.domain.order

import com.zv.brokerageservice.domain.DomainRepository

interface OrderRepository : DomainRepository<OrderId, Order>