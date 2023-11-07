package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class RoutD(
    val id: Int,
    val startingLocationId: Int,
    val finalLocationId: Int,
    val duration: Int
)