package com.findingpartners.event_service.model.response

import com.findingpartners.event_service.enum.OwnerType
import com.findingpartners.event_service.enum.Visibility
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime

data class EventResponse (
    val id: Long,
    val createdAt: LocalDateTime,
    val ownerId: Long,
    val ownerType: OwnerType, // тут enum
    val title: String,
    val description: String? = null,
    val visibility: Visibility,
    val time: Time,
    val date: LocalDate
)