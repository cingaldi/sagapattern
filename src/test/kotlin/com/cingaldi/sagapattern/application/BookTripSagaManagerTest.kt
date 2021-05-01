package com.cingaldi.sagapattern.application

import com.cingaldi.sagapattern.application.commands.BookFlightCmd
import com.cingaldi.sagapattern.application.commands.BookHotelCmd
import com.cingaldi.sagapattern.domain.events.TripCreatedEvt
import com.cingaldi.sagapattern.domain.repositories.TripBookingStatusRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BookTripSagaManagerTest () {

    private var repository: TripBookingStatusRepository = mock()
    private var commandFacade: BookTripCommandFacade = mock()

    private lateinit var sut: BookTripSagaManager

    @BeforeEach
    fun setUp() {
        sut = BookTripSagaManager(repository, commandFacade)
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
}