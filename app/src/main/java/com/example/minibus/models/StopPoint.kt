package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class StopPoint (
    val id : Int,
    val name: String,
    val cityId: Int
)