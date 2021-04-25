package com.cingaldi.commons.flightservice

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class FlightServiceGateway {

    fun bookFlight(flightCode: String) {
        Thread.sleep(Math.random().toLong() * 1000)

        if(Random.nextBoolean()) {
            //emit booked event
        }

        //emit non booked event
    }
}