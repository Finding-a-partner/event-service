package com.findingpartners.event_service

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<EventServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
