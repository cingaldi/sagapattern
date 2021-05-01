package com.cingaldi.commons.flightservice

import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class FlightServiceGateway (
        private val publisher: ApplicationEventPublisher
    ){

    suspend fun bookFlight(flightCode: String) {

        delay(Random.nextLong(3000))

        //TODO uncomment line below if you're ready to play with the unhappy path!
        if(true) { //if(Random.nextBoolean()) {
            publisher.publishEvent(FlightConfirmedEvent(flightCode))
        }

        publisher.publishEvent(FlightCanceledEvent(flightCode))
        yield()
    }
}