package com.example.minibus.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class Time(
    val id : Int,
    @Contextual
    val departureTime: LocalTime,
    @Contextual
    val arrivalTime: LocalTime
)