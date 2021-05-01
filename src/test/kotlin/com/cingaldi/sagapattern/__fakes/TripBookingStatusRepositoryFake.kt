package com.cingaldi.sagapattern.__fakes

import com.cingaldi.sagapattern.domain.models.TripBookingStatus
import com.cingaldi.sagapattern.domain.repositories.TripBookingStatusRepository
import java.util.*

class TripBookingStatusRepositoryFake: TripBookingStatusRepository {

    private var recorded: TripBookingStatus? = null

    override fun findByFlightCode(flightCode: String): Optional<TripBookingStatus> {
        if (recorded?.flightCode != flightCode) {
            return Optional.empty()
        }

        return Optional.ofNullable(recorded)
    }

    override fun findByHotelCode(hotelCode: String): Optional<TripBookingStatus> {
        if (recorded?.hotelCode != hotelCode) {
            return Optional.empty()
        }

        return Optional.ofNullable(recorded)
    }

    override fun <S : TripBookingStatus?> save(p0: S): S {
        recorded = p0
        return recorded as S
    }

    override fun <S : TripBookingStatus?> saveAll(p0: MutableIterable<S>): MutableIterable<S> {
        TODO("Not yet implemented")
    }

    override fun findById(p0: String): Optional<TripBookingStatus> {
        TODO("Not yet implemented")
    }

    override fun existsById(p0: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<TripBookingStatus> {
        TODO("Not yet implemented")
    }

    override fun findAllById(p0: MutableIterable<String>): MutableIterable<TripBookingStatus> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun deleteById(p0: String) {
        TODO("Not yet implemented")
    }

    override fun delete(p0: TripBookingStatus) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(p0: MutableIterable<TripBookingStatus>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }
}