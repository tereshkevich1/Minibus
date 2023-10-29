package com.example.minibus.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.TripTime
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch

class TripViewModel(startingLocationId: Int, finalLocationId: Int, departureDate: String) :
    ViewModel() {

    private var trips: List<TripTime> by mutableStateOf(listOf())
    var isLoading by mutableStateOf(true)

    init {
        viewModelScope.launch {
            trips = MinibusApi.retrofitService.getTrips(
                startingLocationId,
                finalLocationId,
                departureDate
            )
            isLoading = false
        }
    }

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

class MyViewModelFactory(
    private val startingLocationId: Int,
    private val finalLocationId: Int,
    private val departureDate: String
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TripViewModel(startingLocationId, finalLocationId, departureDate) as T
}