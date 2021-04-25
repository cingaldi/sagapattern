package com.cingaldi.sagapattern.domain.repositories

import com.cingaldi.sagapattern.domain.models.TripBookingStatus
import org.springframework.data.repository.CrudRepository
import java.util.*

interface TripBookingStatusRepository: CrudRepository<TripBookingStatus, String> {

    fun findByFlightCode(flightCode: String): Optional<TripBookingStatus>
    fun findByHotelCode(hotelCode: String): Optional<TripBookingStatus>
}