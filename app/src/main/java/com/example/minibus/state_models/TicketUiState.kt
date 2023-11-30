package com.example.minibus.state_models

import java.time.LocalDate

data class TicketUiState (
    val numberAdultsSeats: Int = 1,
    val numberChildrenSeats: Int = 0,
    var departureDate: LocalDate = LocalDate.now(),
    val departureCity: String = "Иваново",
    val arrivalCity: String = "Минск",
    val departurePoint: String? = null,
    val arrivalPoint: String? = null,
    val departurePointId: Int = 1,
    val arrivalPointId: Int = 1,
    val departureCityId: Int = 6,
    val arrivalCityId: Int = 10
)