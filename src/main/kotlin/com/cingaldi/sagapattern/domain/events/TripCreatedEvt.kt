package com.cingaldi.sagapattern.domain.events

data class TripCreatedEvt(
        val tripId: String,
        val flightReservationCode: String,
        val hotelReservationCode: String
)
