package com.cingaldi.sagapattern.application

import com.cingaldi.logger
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import com.cingaldi.sagapattern.domain.models.Trip
import com.cingaldi.sagapattern.domain.repositories.TripRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
@Slf4j
class TripService (
        private val repository: TripRepository,
    ){

    private val logger = logger<TripService>()

    fun createTrip (flightCode: String, hotelCode: String) {
        repository.save(Trip.Factory.createUnconfirmed(flightCode, hotelCode))
    }

    fun confirmTrip (tripId: String) {
        repository.findById(tripId).ifPresent {
            trip -> trip.confirm()
            repository.save(trip)
        }
    }


    fun abortTrip (tripId: String) {
        repository.findById(tripId).ifPresent {
            trip -> trip.abort()
            repository.save(trip)
        }
    }

    @EventListener
    fun onTripCreated (evt: TripCreatedEvt) {
        logger.info("trip was created with id ${evt.tripId}")
    }
}