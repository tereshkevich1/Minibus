package com.example.minibus.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class HistoryViewModel : ViewModel() {

    private val _userTravelHistoryList = MutableStateFlow<List<UserTravelHistory>>(emptyList())
    val userTravelHistoryList = _userTravelHistoryList.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadUserTravelHistoryList()
    }

    private fun loadUserTravelHistoryList() {
        viewModelScope.launch {
            _userTravelHistoryList.value = MinibusApi.retrofitService.getUserTravelHistory(3)
            Log.d("getUserTravelHistory", "${_userTravelHistoryList.value}")
            _isLoading.value = false
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