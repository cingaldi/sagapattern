package com.cingaldi.commons

import com.mysql.cj.x.protobuf.MysqlxExpr
import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents
import java.util.*
import kotlin.collections.ArrayList

abstract class AggregateRoot {

    @Transient
    private val domainEvents = ArrayList<Any>()

    @DomainEvents
    fun domainEvents(): Collection<Any> {
        return Collections.unmodifiableList(domainEvents)
    }

    protected fun registerEvent(event: Any) {
        this.domainEvents.add(event)
    }

    @AfterDomainEventPublication
    protected fun clearDomainEvents() {
        this.domainEvents.clear()
    }

}