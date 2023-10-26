package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class RoutD(
    val id: Int,
    val startingLocation: Int,
    val finalLocation: Int,
    val duration: Int
)