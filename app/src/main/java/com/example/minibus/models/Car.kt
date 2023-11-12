package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Car (
    val id: Int,
    val brandModel: String,
    val numberSeats: Int
)