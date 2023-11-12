package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Transport (
    val bus: Bus,
    val car: Car
)