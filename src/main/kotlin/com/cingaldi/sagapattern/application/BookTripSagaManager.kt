package com.cingaldi.sagapattern.application

import com.cingaldi.commons.flightservice.FlightServiceGateway
import com.cingaldi.commons.hotelservice.HotelConfirmedEvent
import com.cingaldi.commons.hotelservice.HotelServiceGateway
import com.cingaldi.logger
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import com.cingaldi.sagapattern.domain.models.NextAction.CONFIRM_TRIP
import com.cingaldi.sagapattern.domain.models.TripBookingStatus
import com.cingaldi.sagapattern.domain.repositories.TripBookingStatusRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class BookTripSagaManager (
            private val repository: TripBookingStatusRepository,
            private val hotelGateway: HotelServiceGateway,
            private val flightGateway: FlightServiceGateway,
            private val tripService: TripService
        ) {


    private val logger = logger<TripService>()

    /**
     *  this event starts the saga. Thus, its responsibility is to initialize saga status, make data association
     *  and make the first move: flight and hotel booking
     */
    @EventListener
    fun onTripCreated(evt: TripCreatedEvt) {
        repository.save(TripBookingStatus(evt.flightReservationCode, evt.hotelReservationCode, evt.tripId))

        logger.info("trip booking started for tripId=${evt.tripId}")

        hotelGateway.bookHotel(evt.hotelReservationCode)
    }

    @EventListener
    fun onHotelConfirmed(evt: HotelConfirmedEvent) {
        logger.info("hotel booking with code ${evt.code} was confirmed")
        val (nextAction, tripId) = repository
                .findByHotelCode(evt.code)
                .map { status -> Pair(status.bookHotel(), status.tripId)}
                .orElseThrow()

        //if (nextAction == CONFIRM_TRIP) {
            tripService.confirmTrip(tripId)
        //}
    }
}

