package com.cingaldi.sagapattern.application

import com.cingaldi.logger
import com.cingaldi.sagapattern.domain.events.TripConfirmedEvt
import com.cingaldi.sagapattern.domain.models.Trip
import com.cingaldi.sagapattern.domain.repositories.TripRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

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

   @TransactionalEventListener
   fun onTripConfirmed(evt: TripConfirmedEvt) {
       logger.info("dear customer, we are pleased to confirm that your trip with id ${evt.tripId} has been confirmed! Enjoy!")
   }
}