package com.example.minibus.state_models

import java.time.LocalDate

data class TicketUiState (
    val numberAdultsSeats: Int = 1,
    val numberChildrenSeats: Int = 0,
    var departureDate: LocalDate = LocalDate.now(),
    val departureCity: String = "MINSK",
    val arrivalCity: String = "IVANOVO",
    val departurePoint: String? = null,
    val arrivalPoint: String? = null,
    val departurePointId: Int? = null,
    val arrivalPointId: Int? = null,
    val departureCityId: Int = 2,
    val arrivalCityId: Int = 4
)