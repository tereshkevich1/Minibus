package com.example.minibus.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Trip(
    val id: Int,
    val minibusId: Int,
    val driverId: Int,
    val routeId: Int,
    val timeId: Int,
    val price: Int,
    val numberAvailableSeats: Int,
    @Contextual
    val departureDate: LocalDate
)