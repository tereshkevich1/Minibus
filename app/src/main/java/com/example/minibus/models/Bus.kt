package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Bus (
    val id: Int,
    val carNumber: String,
    val carId: Int,
    val yearOfManufacture: Int,
    val carColor: String
)