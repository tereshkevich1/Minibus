package com.example.minibus.vm

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.Trip
import com.example.minibus.models.TripTime
import com.example.minibus.network.MinibusApi
import com.example.minibus.state_models.TicketUiState
import kotlinx.coroutines.launch

class TripViewModel() : ViewModel() {

    private var trips: List<TripTime> by mutableStateOf(listOf())
    var isLoading by mutableStateOf(true)

    fun loadData(startingLocationId: Int, finalLocationId: Int, departureDate: String) {
        // Simulate loading data from a database
        viewModelScope.launch {
            trips = MinibusApi.retrofitService.getTrips(
                startingLocationId,
                finalLocationId,
                departureDate
            )
            isLoading = false
        }
    }

    fun getDataTrips(): List<TripTime> = trips
}