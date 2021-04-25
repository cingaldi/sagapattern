package com.cingaldi.sagapattern.controllers

import com.cingaldi.sagapattern.application.TripService
import com.cingaldi.sagapattern.controllers.vms.CreateTripVM
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TripController(
        private val service: TripService
) {

    @PostMapping("/trips")
    fun createTrip (@RequestBody req : CreateTripVM) : ResponseEntity<Any> {
        service.createTrip(req.flightCode, req.hotelCode)

        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }
}