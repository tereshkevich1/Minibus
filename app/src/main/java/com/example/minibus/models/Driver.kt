package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    val id : Int,
    val driverLicense: Int
)
