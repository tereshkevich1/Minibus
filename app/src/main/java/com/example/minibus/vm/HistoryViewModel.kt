package com.example.minibus.vm

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
    var tripHistoryUIState by mutableStateOf<MinibusUiState<List<UserTravelHistory>>>(MinibusUiState.Loading)

    init {
        loadUserTravelHistoryList()
    }

    fun loadUserTravelHistoryList() {
        tripHistoryUIState = MinibusUiState.Loading
        viewModelScope.launch {

            tripHistoryUIState = try {

                val userTravelHistoryList = MinibusApi.retrofitService.getUserTravelHistory(3)
                MinibusUiState.Success(userTravelHistoryList)

            } catch (e: IOException) {

                MinibusUiState.Error(e)

            } catch (e: HttpException) {
                MinibusUiState.Error(e)
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