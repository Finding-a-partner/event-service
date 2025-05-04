package com.findingpartners.event_service.controller

import com.findingpartners.event_service.model.request.EventRequest
import com.findingpartners.event_service.model.response.OwnerResponse
import com.findingpartners.event_service.service.EventService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventController(
    val eventService: EventService
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long) = eventService.getById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody request: EventRequest) = eventService.update(id, request)

    @PostMapping
    fun create(@RequestBody request: EventRequest) = eventService.create(request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long) =
        eventService.delete(id)

    @GetMapping("/{ownerId}")
    fun getByOwnerId(@PathVariable("ownerId") id: Long) = eventService.getByOwnerId(id)

    @GetMapping("/{eventId}/owner")
    fun getEventOwner(@PathVariable eventId: Long): OwnerResponse {
        return eventService.getEventOwner(eventId)
    }

}