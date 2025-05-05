package com.findingpartners.event_service.service.impl

import com.findingpartners.event_service.database.entity.Event
import com.findingpartners.event_service.database.repository.EventDao
import com.findingpartners.event_service.enum.OwnerType
import com.findingpartners.event_service.model.request.EventRequest
import com.findingpartners.event_service.model.response.EventResponse
import com.findingpartners.event_service.model.response.OwnerResponse
import com.findingpartners.event_service.service.EventService
import com.findingpartners.event_service.service.client.GroupServiceClient
import com.findingpartners.event_service.service.client.UserServiceClient
import com.findingpartners.event_service.util.EventMapper
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service

@Service
class EventServiceImpl(
    val dao: EventDao,
    val mapper: EventMapper,
    val userServiceClient: UserServiceClient, // Feign-клиент к сервису пользователей
    val groupServiceClient: GroupServiceClient
) : EventService {
    override fun update(id: Long, request: EventRequest): EventResponse {
        val entity = dao.findById(id).orElseThrow { throw RuntimeException("") }
            .apply{
                ownerId = request.ownerId
                ownerType = request.ownerType
                title = request.title
                description = request.description
                visibility = request.visibility
                time = request.time
                date = request.date
            }
        val updatedEntity = dao.save(entity)  // Явно сохраняем изменения
        return mapper.entityToResponse(entity)
    }

    override fun delete(id: Long) {
        val entity = dao.findById(id).orElseThrow { throw RuntimeException("") }
        dao.delete(entity)
    }

    override fun create (request: EventRequest): EventResponse {
        val entity = Event(
            ownerId = request.ownerId,
            ownerType = request.ownerType,
            title = request.title,
            description = request.description,
            visibility = request.visibility,
            time = request.time,
            date = request.date
        )
        return mapper.entityToResponse(dao.save(entity))
    }

    override fun getById(id: Long): EventResponse {
        return mapper.entityToResponse(dao.findById(id).orElseThrow(){ throw RuntimeException("")})
    }

    override fun getByOwnerId (id: Long, type: OwnerType): List<EventResponse>{
        val events = dao.findAllByOwnerIdAndOwnerType(id, type)

        return events.map { it -> mapper.entityToResponse(it)}
    }

    override fun getEventOwner(eventId: Long): OwnerResponse {
        val event = dao.findById(eventId)
            .orElseThrow { NotFoundException("Event not found") }

        return when (event.ownerType) {
            OwnerType.USER -> {
                val user = userServiceClient.getById(event.ownerId)
                OwnerResponse(
                    id = user.id,
                    type = OwnerType.USER,
                    login = user.login,
                    name = user.name,
                    surname = user.surname
                )
            }
            OwnerType.GROUP -> {
                val group = groupServiceClient.getGroupById(event.ownerId)
                OwnerResponse(
                    id = group.id,
                    type = OwnerType.GROUP,
                    name = group.name

                )
            }
        }
    }

}