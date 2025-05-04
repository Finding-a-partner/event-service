package com.findingpartners.event_service.database.repository

import com.findingpartners.event_service.database.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventDao: JpaRepository<Event, Long> {
    fun findAllByOwnerId(id: Long) : List<Event>
}