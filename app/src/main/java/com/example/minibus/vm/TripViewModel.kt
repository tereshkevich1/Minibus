package com.example.minibus.vm

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.TripTime
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TripViewModel(startingLocationId: Int, finalLocationId: Int, departureDate: String) :
    ViewModel() {


    var tripUIState by mutableStateOf<MinibusUiState<List<TripTime>>>(MinibusUiState.Loading)

    init {
        loadData(startingLocationId, finalLocationId, departureDate)
    }

    fun loadData(startingLocationId: Int, finalLocationId: Int, departureDate: String) {
        tripUIState = MinibusUiState.Loading
        viewModelScope.launch {

            tripUIState = try {
                val trips = MinibusApi.retrofitService.getTrips(
                    startingLocationId,
                    finalLocationId,
                    departureDate
                )
                MinibusUiState.Success(trips)

            } catch (e: IOException) {
                MinibusUiState.Error(e)

            } catch (e: HttpException) {
                MinibusUiState.Error(e)
            }
        }
    }

    fun printNumberSeats(numberSeats: Int): String {
        return if (numberSeats <= 5) {
            "$numberSeats "
        } else "5+"
    }

    @Composable
    fun printColorSeats(numberSeats: Int): Color {
        return if (numberSeats <= 5) {
            MaterialTheme.colorScheme.error
        } else MaterialTheme.colorScheme.primary
    }

    fun getDataTrips(): List<TripTime> {
        return if (tripUIState is MinibusUiState.Success<*>) {
            (tripUIState as MinibusUiState.Success<List<TripTime>>).data
        } else {
            emptyList()
        }
    }

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