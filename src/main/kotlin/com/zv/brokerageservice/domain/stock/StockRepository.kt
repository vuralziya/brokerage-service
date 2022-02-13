package com.zv.brokerageservice.domain.stock

import com.zv.brokerageservice.domain.DomainRepository

interface StockRepository : DomainRepository<String, Stock>