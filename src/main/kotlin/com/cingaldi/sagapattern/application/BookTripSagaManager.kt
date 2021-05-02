package com.cingaldi.sagapattern.application

import com.cingaldi.commons.Saga
import com.cingaldi.commons.flightservice.FlightConfirmedEvent
import com.cingaldi.commons.hotelservice.HotelConfirmedEvent
import com.cingaldi.logger
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import com.cingaldi.sagapattern.domain.models.TripBookingStatus
import com.cingaldi.sagapattern.domain.repositories.TripBookingStatusRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class BookTripSagaManager (
            private val repository: TripBookingStatusRepository,
            private val commandFacade: BookTripCommandFacade
        ) {

    private val logger = logger<BookTripSagaManager>()

    /**
     *  this event starts the saga. Thus, its responsibility is to initialize saga status, make data association
     *  and make the first move: flight and hotel booking
     */
    @EventListener
    fun on(evt: TripCreatedEvt) {

        //create saga
        val saga = TripBookingStatus(evt.flightReservationCode, evt.hotelReservationCode, evt.tripId)

        saga.start()

        //persist
        logger.debug("trip booking started for tripId=${evt.tripId}")
        update(saga)

        //set deadline
        GlobalScope.launch { scheduleDeadline(saga.hotelCode, 6000) }
    }

    @EventListener
    fun on(evt: FlightConfirmedEvent) {
        
        //retrieve saga
        val saga = repository.findByFlightCode(evt.code).orElseThrow()

        //update the progress
        saga.bookFlight()

        //persist
        logger.debug("flight booking with code ${evt.code} was confirmed")
        update(saga)
    }

    @EventListener
    fun on(evt: HotelConfirmedEvent) {

        //retrieve saga
        val saga = repository.findByHotelCode(evt.code).orElseThrow()

        //update the progress
        saga.bookHotel()

        //persist
        logger.debug("hotel booking with code ${evt.code} was confirmed")
        update(saga)
    }

    private fun update(saga: TripBookingStatus) {
        repository.save(saga)
        saga.commands().forEach {
            //send command
            cmd ->
            commandFacade.dispatch(cmd)
        }
        saga.clearCommands()
    }

    /**
     *  notice! this way to schedule a delayed task is not safe at all!
     *  the scheduling won't survive to a service reboot
     */
    private suspend fun scheduleDeadline(hotelCode: String, delayMillis: Long) {
        delay(delayMillis)
        repository.findByHotelCode(hotelCode).ifPresent { saga ->
            if(!saga.isCompleted()) {
                logger.debug("process still ongoing. Wrap it up somehow!!!!")
            }
        }

    }
}

