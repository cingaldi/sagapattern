package com.cingaldi.sagapattern.domain.repositories

import com.cingaldi.sagapattern.domain.models.Trip
import org.springframework.data.repository.CrudRepository

interface TripRepository: CrudRepository<Trip, String> {
}