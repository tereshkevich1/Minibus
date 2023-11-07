package com.example.minibus.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
            _userTravelHistoryList.value = MinibusApi.retrofitService.getUserTravelHistory(4)
            Log.d("getUserTravelHistory","${_userTravelHistoryList.value}")
        }
    }
}