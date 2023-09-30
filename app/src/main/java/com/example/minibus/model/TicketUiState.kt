package com.example.minibus.model

import java.util.Date

data class TicketUiState (
    val numberAdultsSeats: Int = 0,
    val numberChildrenSeats: Int = 0,
    val departureDate: Date = Date(),
    val departureCity: String = ""
)