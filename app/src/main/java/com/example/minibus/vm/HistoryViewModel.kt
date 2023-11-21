package com.example.minibus.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class HistoryViewModel : ViewModel() {
    var tripHistoryUIState by mutableStateOf<TripHistoryUiState<List<UserTravelHistory>>>(
        TripHistoryUiState.Loading
    )

    init {
        loadUserTravelHistoryList()
    }

    fun loadUserTravelHistoryList() {
        tripHistoryUIState = TripHistoryUiState.Loading
        viewModelScope.launch {

            tripHistoryUIState = try {

                val tripsList =  MinibusApi.retrofitService.getUserTravelHistory(4)

                val futureTrips = tripsList.filter { it.order.status == 1 }
                val pastTrips =  tripsList.filter { it.order.status == 2 }

                TripHistoryUiState.Success(futureTrips, pastTrips)

            } catch (e: IOException) {

                Log.d("whattt", "$e")
                TripHistoryUiState.Error(e)

            } catch (e: HttpException) {
                Log.d("whattt", "$e")
                TripHistoryUiState.Error(e)

            }

        }
    }


    fun setDuration(startTime: LocalTime, endTime: LocalTime): String {
        val duration = if (startTime.isBefore(endTime)) {
            ChronoUnit.MINUTES.between(startTime, endTime)
        } else {
            ChronoUnit.MINUTES.between(startTime, LocalTime.MAX).plus(1) +
                    ChronoUnit.MINUTES.between(LocalTime.MIN, endTime)
        }

        val hours = duration / 60
        val minutes = duration % 60

        return "${hours}ч ${minutes}мин"
    }
}