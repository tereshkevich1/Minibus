package com.example.minibus.models

import Order
import kotlinx.serialization.Serializable

@Serializable
data class UserTravelHistory(
    val order: Order,
    val trip: Trip,
    val time: Time,
    val route: RoutD,
    val departureCity: String,
    val arrivalCity: String,
    val departurePoint: String,
    val arrivalPoint: String
)
