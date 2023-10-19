package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class Rout(
    val id: Int,
    val startingLocation: String,
    val finalLocation: String,
    val distance: Int,
    val duration: Int
)