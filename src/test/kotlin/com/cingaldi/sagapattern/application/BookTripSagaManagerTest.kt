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

    //fixtures
    val TRIP_ID : String = "aaa"
    val FLIGHT_CODE: String = "bbb"
    val HOTEL_CODE: String = "ccc"

    //deps
    private var commandFacade: BookTripCommandFacade = mock()

    //sut
    private lateinit var sut: BookTripSagaManager

    @BeforeEach
    fun setUp() {
        sut = BookTripSagaManager(TripBookingStatusRepositoryFake(), commandFacade)
    }

    @Test
    fun `given trip - when created - then book flight` () {
        sut.on(TripCreatedEvt(TRIP_ID, FLIGHT_CODE, HOTEL_CODE))
        verify(commandFacade).dispatch(any<BookFlightCmd>())
    }


    @Test
    fun `given trip - when created - then book hotel` () {
        sut.on(TripCreatedEvt(TRIP_ID, FLIGHT_CODE, HOTEL_CODE))
        verify(commandFacade).dispatch(any<BookHotelCmd>())
    }

    @Test
    fun `given flight booked - when hotel booked - then confirm trip` () {

        //given
        sut.on(TripCreatedEvt(TRIP_ID, FLIGHT_CODE, HOTEL_CODE))
        sut.on(FlightConfirmedEvent(FLIGHT_CODE))

        //when
        sut.on(HotelConfirmedEvent(HOTEL_CODE))

        //then
        verify(commandFacade).dispatch(any<ConfirmTripCmd>())

    }

    @Test
    fun `given hotel booked - when flight booked - then confirm trip` () {

        //given
        sut.on(TripCreatedEvt(TRIP_ID, FLIGHT_CODE, HOTEL_CODE))
        sut.on(HotelConfirmedEvent(HOTEL_CODE))

        //when
        sut.on(FlightConfirmedEvent(FLIGHT_CODE))

        //then
        verify(commandFacade).dispatch(any<ConfirmTripCmd>())

    }
}