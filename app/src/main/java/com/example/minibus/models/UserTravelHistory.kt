package com.example.minibus.models

import Order
import kotlinx.serialization.Serializable

@Serializable
data class UserTravelHistory(
    val order: Order,
    val trip: Trip,
    val minibus: Bus,
    val time: Time,
    val route: RoutD
)
