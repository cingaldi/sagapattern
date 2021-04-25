package com.cingaldi.sagapattern.domain.models

import com.cingaldi.commons.AggregateRoot
import com.cingaldi.sagapattern.domain.events.TripAbortedEvt
import com.cingaldi.sagapattern.domain.events.TripConfirmedEvt
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Trip(): AggregateRoot() {

    @Id
    var id: String = ""
        private set

    private var flightCode: String = ""
    private var hotelCode: String = ""
    private var isConfirmed: Boolean = false

    fun confirm() {
        isConfirmed = true

        registerEvent(TripConfirmedEvt(id))
    }

    fun abort() {
        if(isConfirmed) {
            throw Exception("You cannot abort a confirmed trip")
        }

        registerEvent(TripAbortedEvt(id))
    }

    /**
     * let's just add a method which checks an invariant to show the final purpose of the whole thing
     */
    fun editFlight(newFlightCode: String) {
        if (isConfirmed) {
            throw Exception("You cannot edit a confirmed trip!")
        }

        flightCode = newFlightCode
    }

    object Factory {
        fun createUnconfirmed(flightCode: String, hotelCode: String) : Trip {
            val trip = Trip()
            trip.id  = UUID.randomUUID().toString()
            trip.flightCode = flightCode
            trip.hotelCode = hotelCode
            trip.isConfirmed = false

            trip.registerEvent(
                    TripCreatedEvt(trip.id, trip.flightCode, trip.hotelCode)
            )

            return trip
        }
    }
}
