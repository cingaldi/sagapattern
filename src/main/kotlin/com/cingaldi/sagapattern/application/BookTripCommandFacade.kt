package com.cingaldi.sagapattern.application

import com.cingaldi.commons.flightservice.FlightServiceGateway
import com.cingaldi.commons.hotelservice.HotelServiceGateway
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

    fun apply(cmd: BookHotelCmd) : BookTripCommandFacade{
        hotelGateway.bookHotel(cmd.hotelCode)
        return this;
    }


    fun apply(cmd: BookFlightCmd) : BookTripCommandFacade{
        flightGateway.bookFlight(cmd.flightCode)
        return this;
    }

    fun apply(cmd: ConfirmTripCmd) : BookTripCommandFacade{
        tripService.confirmTrip(cmd.tripId)
        return this;
    }
}