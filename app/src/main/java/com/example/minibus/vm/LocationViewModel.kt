package com.example.minibus.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.City
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch


class LocationViewModel : ViewModel() {
    private var cityData: List<City> by mutableStateOf(listOf())
    var isLoading by mutableStateOf(true)


    init {
        loadData()
    }

    private fun loadData() {
        // Simulate loading data from a database
        viewModelScope.launch {
            cityData = MinibusApi.retrofitService.getPhotos()
            isLoading = false
        }
    }

    fun getData(): List<City> = cityData
}