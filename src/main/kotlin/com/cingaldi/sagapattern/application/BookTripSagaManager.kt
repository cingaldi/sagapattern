package com.cingaldi.sagapattern.application

import com.cingaldi.commons.flightservice.FlightConfirmedEvent
import com.cingaldi.commons.flightservice.FlightServiceGateway
import com.cingaldi.commons.hotelservice.HotelConfirmedEvent
import com.cingaldi.commons.hotelservice.HotelServiceGateway
import com.cingaldi.logger
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import com.cingaldi.sagapattern.domain.models.NextAction
import com.cingaldi.sagapattern.domain.models.NextAction.*
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

    private val logger = logger<BookTripSagaManager>()

    /**
     *  this event starts the saga. Thus, its responsibility is to initialize saga status, make data association
     *  and make the first move: flight and hotel booking
     */
    @EventListener
    fun onTripCreated(evt: TripCreatedEvt) {

        //create saga
        val saga = TripBookingStatus(evt.flightReservationCode, evt.hotelReservationCode, evt.tripId)

        //persist
        logger.debug("trip booking started for tripId=${evt.tripId}")
        repository.save(saga)

        //send command
        hotelGateway.bookHotel(evt.hotelReservationCode)
        flightGateway.bookFlight(evt.flightReservationCode)
    }

    @EventListener
    fun onFlightConfirmed(evt: FlightConfirmedEvent) {
        
        //retrieve saga
        val saga = repository
                .findByFlightCode(evt.code)
                .orElseThrow()

        //update the progress
        val nextAction = saga.bookFlight()

        logger.debug("flight booking with code ${evt.code} was confirmed. Next action=$nextAction")
        repository.save(saga)

        //send command
        performNextAction(nextAction, saga)
    }

    @EventListener
    fun onHotelConfirmed(evt: HotelConfirmedEvent) {

        //retrieve saga
        val saga = repository
                .findByHotelCode(evt.code)
                .orElseThrow()

        //update the progress
        val nextAction = saga.bookHotel()

        logger.debug("hotel booking with code ${evt.code} was confirmed. Next action=$nextAction")
        repository.save(saga)

        //send command
        performNextAction(nextAction, saga)
    }

    private fun performNextAction(nextAction: NextAction, saga : TripBookingStatus) {
        when (nextAction) {
            CONFIRM_TRIP -> tripService.confirmTrip(saga.tripId)
        }
    }
}

