package com.example.minibus.model

import java.time.LocalDate

data class TicketUiState (
    val numberAdultsSeats: Int = 1,
    val numberChildrenSeats: Int = 0,
    var departureDate: LocalDate = LocalDate.now(),
    val departureCity: String = "MINSK",
    val arrivalCity: String = "IVANOVO"
)