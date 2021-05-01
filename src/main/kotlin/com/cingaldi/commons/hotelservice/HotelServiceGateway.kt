package com.cingaldi.commons.hotelservice

import kotlinx.coroutines.delay
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class HotelServiceGateway (
        private val publisher: ApplicationEventPublisher
    ){

    suspend fun bookHotel(hotelCode: String) {
        delay(Random.nextLong(3000))

        //TODO uncomment line below if you're ready to play with the unhappy path!
        if(true) { //if(Random.nextBoolean()) {
            publisher.publishEvent(HotelConfirmedEvent(hotelCode))
        }

        publisher.publishEvent(HotelCanceledEvent(hotelCode))
    }
}