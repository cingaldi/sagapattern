package com.cingaldi.commons.hotelservice

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class HotelServiceGateway (
        private val publisher: ApplicationEventPublisher
    ){

    fun bookHotel(hotelCode: String) {
        Thread.sleep(Math.random().toLong() * 1000)

        if(Random.nextBoolean()) {
            publisher.publishEvent(HotelConfirmedEvent(hotelCode))
        }

        publisher.publishEvent(HotelCanceledEvent(hotelCode))

    }
}