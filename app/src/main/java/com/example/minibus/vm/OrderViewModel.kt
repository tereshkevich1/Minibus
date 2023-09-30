package com.example.minibus.vm

import androidx.lifecycle.ViewModel
import com.example.minibus.model.TicketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState = _uiState.asStateFlow()

    // 4fun for AddPassengersScreen--
    fun increaseNumberAdultsSeats() {
        val updatedNumber = uiState.value.numberAdultsSeats.plus(1)
        _uiState.update { currentState -> currentState.copy(numberAdultsSeats = updatedNumber) }
    }

    fun decreaseNumberAdultsSeats() {
        val updatedNumber = uiState.value.numberAdultsSeats.minus(1)
        if (updatedNumber >= 0) {
            _uiState.update { currentState -> currentState.copy(numberAdultsSeats = updatedNumber) }
        }
    }

    fun increaseNumberChildrenSeats() {
        val updatedNumber = uiState.value.numberChildrenSeats.plus(1)
        _uiState.update { currentState -> currentState.copy(numberChildrenSeats = updatedNumber) }
    }

    fun decreaseNumberChildrenSeats() {
        val updatedNumber = uiState.value.numberChildrenSeats.minus(1)
        if (updatedNumber >= 0) {
            _uiState.update { currentState -> currentState.copy(numberChildrenSeats = updatedNumber) }
        }
    }
    //--

    fun setDepartureCity(departureCity: String){
        _uiState.update { currentState -> currentState.copy(departureCity = departureCity) }
    }
}