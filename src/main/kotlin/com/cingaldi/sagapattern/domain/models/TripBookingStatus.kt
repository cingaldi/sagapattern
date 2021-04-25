package com.cingaldi.sagapattern.domain.models


import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
class TripBookingStatus () {

    //association data
    @Id
    var tripId: String = ""
        private set
    var flightCode: String = ""
        private set
    var hotelCode: String = ""
        private set

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

    fun bookFlight() : NextAction {
        flightBooked = true

        if(hotelBooked) {
            return NextAction.CONFIRM_TRIP
        }

        return NextAction.BOOK_HOTEL
    }

    fun bookHotel() :NextAction{
        hotelBooked = true

        if(flightBooked) {
            return NextAction.CONFIRM_TRIP
        }

        return NextAction.BOOK_FLIGHT
    }

    fun unbookFlight(): NextAction {
        flightBooked = false

        if(!hotelBooked) {
            return NextAction.ABORT_TRIP
        }

        return NextAction.CANCEL_HOTEL
    }

    fun unbookHotel(): NextAction {
        hotelBooked = false

        if(!flightBooked) {
            return NextAction.ABORT_TRIP
        }

        return NextAction.CANCEL_FLIGHT
    }

    fun start(): NextAction {
        return NextAction.BOOK_FLIGHT
    }
}

enum class NextAction {
    BOOK_FLIGHT,
    BOOK_HOTEL,
    CONFIRM_TRIP,
    CANCEL_FLIGHT,
    CANCEL_HOTEL,
    ABORT_TRIP,
}