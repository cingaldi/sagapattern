package com.cingaldi.sagapattern.domain.models


import com.cingaldi.commons.Saga
import com.cingaldi.sagapattern.application.commands.BookFlightCmd
import com.cingaldi.sagapattern.application.commands.BookHotelCmd
import com.cingaldi.sagapattern.application.commands.ConfirmTripCmd
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
class TripBookingStatus(): Saga() {

    //association data
    @Id
    var tripId: String = ""
        private set
    var flightCode: String = ""
        private set
    var hotelCode: String = ""
        private set

    //this saga supports concurrent events :)
    @Version
    private var version: Long = 0

    //saga progress data
    private var flightBooked: Boolean = false
    private var hotelBooked: Boolean = false

    constructor(_flightCode: String, _hotelCode: String, _tripId: String): this() {
        flightCode = _flightCode
        hotelCode = _hotelCode
        tripId = _tripId
    }

    fun start() {
        dispatchCommand(BookFlightCmd(flightCode))
        dispatchCommand(BookHotelCmd(hotelCode))
    }

    fun bookFlight() {
        flightBooked = true

        if(hotelBooked) {
            dispatchCommand(ConfirmTripCmd(tripId))
            complete()
        }
    }

    fun bookHotel() {
        hotelBooked = true

        if(flightBooked) {
            dispatchCommand(ConfirmTripCmd(tripId))
            complete()
        }
    }

    fun unbookFlight() {
        flightBooked = false

        if(!hotelBooked) {
            // just abort trip ;)
        }

        // need to cancel the hotel as well ;)

    }

    fun unbookHotel() {
        hotelBooked = false

        //you know how to do it
    }
}