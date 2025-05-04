package com.findingpartners.event_service.database.entity

import com.findingpartners.event_service.enum.OwnerType
import com.findingpartners.event_service.enum.Visibility
import jakarta.persistence.*
import java.sql.Date
import java.sql.Time
import java.time.LocalDate

@Entity
@Table(name = "event")
class Event(
    @Column(name = "owner_id")
    var ownerId: Long,

    @Column(name = "owner_type")
    @Enumerated(EnumType.STRING)
    var ownerType: OwnerType, // тут тоже enum

    @Column(nullable = false)
    var title: String,

    @Column
    var description: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var visibility: Visibility, // скорее всего enum будет

    @Column
    var date: LocalDate,

    @Column
    var time: Time
) : AbstractEntity()