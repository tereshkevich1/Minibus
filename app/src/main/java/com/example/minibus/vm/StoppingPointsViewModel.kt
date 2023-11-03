package com.example.minibus.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.StopPoint
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoppingPointsViewModel(val cityId: Int) : ViewModel() {
    private val _stoppingPointsData = MutableStateFlow<List<StopPoint>>(emptyList())
    val stoppingPointsData: StateFlow<List<StopPoint>> = _stoppingPointsData

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        loadData()
    }

    private fun loadData() {
        // Simulate loading data from a database
        viewModelScope.launch {
            Log.d("getDataSPVM","data loaded")
            _stoppingPointsData.value = MinibusApi.retrofitService.getStopsByCityId(cityId)
            _isLoading.value = false
        }
    }
}

class StoppingPointsViewModelFactory(private val cityId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StoppingPointsViewModel(cityId) as T
}

