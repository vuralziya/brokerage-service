package com.zv.brokerageservice.domain.user

import com.zv.brokerageservice.domain.DomainRepository

interface UserRepository : DomainRepository<UserId, User>