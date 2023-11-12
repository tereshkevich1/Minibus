package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Details (
    val trip: Trip,
    val minibus: Bus,
    val time: Time,
    val busInfo: Car
)