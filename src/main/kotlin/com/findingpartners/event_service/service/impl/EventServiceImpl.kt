package com.findingpartners.event_service.service.impl

import com.findingpartners.event_service.database.entity.Event
import com.findingpartners.event_service.database.entity.EventMembers
import com.findingpartners.event_service.database.repository.EventDao
import com.findingpartners.event_service.database.repository.EventMembersDao
import com.findingpartners.event_service.enum.OwnerType
import com.findingpartners.event_service.errors.ResourceNotFoundException
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
    val groupServiceClient: GroupServiceClient,
    val memberDao: EventMembersDao,
) : EventService {
    override fun update(id: Long, request: EventRequest): EventResponse {
        val entity = dao.findById(id).orElseThrow { throw RuntimeException("") }
            .apply {
                ownerId = request.ownerId
                ownerType = request.ownerType
                title = request.title
                description = request.description
                visibility = request.visibility
                time = request.time
                date = request.date
            }
        val updatedEntity = dao.save(entity) // Явно сохраняем изменения
        return mapper.entityToResponse(entity)
    }

    override fun delete(id: Long) {
        val entity = dao.findById(id).orElseThrow { throw RuntimeException("") }
        dao.delete(entity)
    }

    override fun create(request: EventRequest, userId: Long): EventResponse {
        val event = Event(
            ownerId = request.ownerId,
            ownerType = request.ownerType,
            title = request.title,
            description = request.description,
            visibility = request.visibility,
            time = request.time,
            date = request.date,
        )
        val savedEvent = dao.save(event)

        val owner = EventMembers(
            userId = request.ownerId,
            event = event,
        )
        memberDao.save(owner)

        return mapper.entityToResponse(dao.save(savedEvent))
    }

    override fun getAll(): List<EventResponse> {
        return dao.findAll().map { mapper.entityToResponse(it) }
    }

    override fun getById(id: Long): EventResponse {
        return mapper.entityToResponse(dao.findById(id).orElseThrow { throw ResourceNotFoundException(id) })
    }

    override fun getByOwnerId(id: Long): List<EventResponse> {
        val events = dao.findAllByOwnerId(id)

        return events.map { it -> mapper.entityToResponse(it) }
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
                    surname = user.surname,
                )
            }
            OwnerType.GROUP -> {
                val group = groupServiceClient.getGroupById(event.ownerId)
                OwnerResponse(
                    id = group.id,
                    type = OwnerType.GROUP,
                    name = group.name,

                )
            }
        }
    }
}
