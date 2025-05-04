package com.findingpartners.event_service.model.request

import com.findingpartners.event_service.enum.OwnerType
import com.findingpartners.event_service.enum.Visibility
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class EventRequest (
    var ownerId: Long,
    var ownerType: OwnerType,
    var title: String,
    var description: String? = null,
    var visibility: Visibility,
    var time: Time,
    var date: LocalDate
)