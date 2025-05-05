package com.findingpartners.event_service.database.repository

import com.findingpartners.event_service.database.entity.Event
import com.findingpartners.event_service.enum.OwnerType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventDao: JpaRepository<Event, Long> {
    fun findAllByOwnerIdAndOwnerType(id: Long, type: OwnerType) : List<Event>
}