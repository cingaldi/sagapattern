package com.cingaldi.sagapattern.application

import com.cingaldi.commons.flightservice.FlightConfirmedEvent
import com.cingaldi.commons.hotelservice.HotelConfirmedEvent
import com.cingaldi.sagapattern.__fakes.TripBookingStatusRepositoryFake
import com.cingaldi.sagapattern.application.commands.BookFlightCmd
import com.cingaldi.sagapattern.application.commands.BookHotelCmd
import com.cingaldi.sagapattern.application.commands.ConfirmTripCmd
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BookTripSagaManagerTest () {

    private var commandFacade: BookTripCommandFacade = mock()

    private lateinit var sut: BookTripSagaManager

    @BeforeEach
    fun setUp() {
        sut = BookTripSagaManager(TripBookingStatusRepositoryFake(), commandFacade)
    }

    @Test
    fun `given trip - when created - then book flight` () {
        sut.onTripCreated(TripCreatedEvt("a","b", "c"))
        verify(commandFacade).dispatch(any<BookFlightCmd>())
    }


    @Test
    fun `given trip - when created - then book hotel` () {
        sut.onTripCreated(TripCreatedEvt("a","b", "c"))
        verify(commandFacade).dispatch(any<BookHotelCmd>())
    }

    @Test
    fun `given flight booked - when hotel booked - then confirm trip` () {

        //given
        sut.onTripCreated(TripCreatedEvt("a","b", "c"))
        sut.onFlightConfirmed(FlightConfirmedEvent("b"))

        //when
        sut.onHotelConfirmed(HotelConfirmedEvent("c"))

        //then
        verify(commandFacade).dispatch(any<ConfirmTripCmd>())

    }

    @Test
    fun `given hotel booked - when flight booked - then confirm trip` () {

        //given
        sut.onTripCreated(TripCreatedEvt("a","b", "c"))
        sut.onHotelConfirmed(HotelConfirmedEvent("c"))

        //when
        sut.onFlightConfirmed(FlightConfirmedEvent("b"))

        //then
        verify(commandFacade).dispatch(any<ConfirmTripCmd>())

    }
}