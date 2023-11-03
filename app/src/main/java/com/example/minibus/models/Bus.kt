package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Bus (
    val id: Int,
    val carColor: String,
    val numberSeats: Int,
    val carName: String,
    val carNumber: String
)