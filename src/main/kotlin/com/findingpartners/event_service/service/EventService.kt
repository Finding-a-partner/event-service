package com.findingpartners.event_service.service

import com.findingpartners.event_service.model.request.EventRequest
import com.findingpartners.event_service.model.response.EventResponse
import com.findingpartners.event_service.model.response.OwnerResponse

interface EventService {
    fun update(id: Long, request: EventRequest) : EventResponse
    fun delete(id: Long)
    fun create (request: EventRequest) : EventResponse
    fun getById (id: Long): EventResponse
    fun getByOwnerId (id: Long): List <EventResponse>
    fun getEventOwner(eventId: Long): OwnerResponse
}