package com.cingaldi.sagapattern.application

import com.cingaldi.commons.flightservice.FlightServiceGateway
import com.cingaldi.commons.hotelservice.HotelServiceGateway
import com.cingaldi.logger
import com.cingaldi.sagapattern.application.commands.BookFlightCmd
import com.cingaldi.sagapattern.application.commands.BookHotelCmd
import com.cingaldi.sagapattern.application.commands.ConfirmTripCmd
import org.springframework.stereotype.Component

@Component
class BookTripCommandFacade(
        private val hotelGateway: HotelServiceGateway,
        private val flightGateway: FlightServiceGateway,
        private val tripService: TripService
    ) {

    fun dispatch (cmd: Any): BookTripCommandFacade {

        when(cmd) {
            is BookFlightCmd -> flightGateway.bookFlight(cmd.flightCode)
            is BookHotelCmd -> hotelGateway.bookHotel(cmd.hotelCode)
            is ConfirmTripCmd -> tripService.confirmTrip(cmd.tripId)
            else -> throw Exception("command handler not defined for command type ${cmd.javaClass}")
        }

        return this;
    }
}