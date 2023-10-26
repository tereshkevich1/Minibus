package com.example.minibus.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class TripTime(
    val id: Int,
    val minibusId: Int,
    val driverId: Int,
    val routeId: Int,
    val timeId: Int,
    val price: Int,
    val numberAvailableSeats: Int,
    @Contextual
    val departureDate: LocalDate,
    @Contextual
    val departureTime: LocalTime,
    @Contextual
    val arrivalTime: LocalTime
)
