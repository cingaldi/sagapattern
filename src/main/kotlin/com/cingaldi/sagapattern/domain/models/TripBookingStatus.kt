package com.cingaldi.sagapattern.domain.models


import com.cingaldi.commons.Saga
import com.cingaldi.sagapattern.application.commands.BookFlightCmd
import com.cingaldi.sagapattern.application.commands.BookHotelCmd
import com.cingaldi.sagapattern.application.commands.ConfirmTripCmd
import java.io.Serializable
import javax.persistence.*

@Entity
@IdClass(TripBookingId::class)
class TripBookingStatus(): Saga() {

    //association data
    @Id
    lateinit var flightCode: String
        private set
    @Id
    lateinit var hotelCode: String
        private set
    @Id
    lateinit var tripId: String
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

@Embeddable
class TripBookingId (): Serializable {

    lateinit var hotelCode: String
        private set
    lateinit var flightCode : String
        private set
    lateinit var tripId: String
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TripBookingId

        if (hotelCode != other.hotelCode) return false
        if (flightCode != other.flightCode) return false
        if (tripId != other.tripId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hotelCode.hashCode()
        result = 31 * result + flightCode.hashCode()
        result = 31 * result + tripId.hashCode()
        return result
    }


}