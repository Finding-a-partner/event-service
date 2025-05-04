package com.findingpartners.event_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class EventServiceApplication

fun main(args: Array<String>) {
	runApplication<EventServiceApplication>(*args)
}
