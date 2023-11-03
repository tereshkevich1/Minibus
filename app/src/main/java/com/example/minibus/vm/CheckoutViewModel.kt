package com.example.minibus.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.Details
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch

class CheckoutViewModel(tripId: Int) : ViewModel() {
    private var orderDetailsObject: Details? by mutableStateOf(null)

    var isLoading by mutableStateOf(true)

    init {
        viewModelScope.launch {
            orderDetailsObject = MinibusApi.retrofitService.getOrderDetails(tripId)
            isLoading = false
        }
    }

    private fun loadData() {
        // Simulate loading data from a database
        viewModelScope.launch {


            /* trip = MinibusApi.retrofitService.getTripbyId(id)
             if (trip!=null){
                 driver = MinibusApi.retrofitService.getDriverById(trip.driverId)
                 time = MinibusApi.retrofitService.getDriverById(trip.timeId)
             }
             isLoading = false*/
        }
    }

    fun getData(): Details? = orderDetailsObject
    //    fun getDriver(): Trip = driver

    //   fun getDriver(): Trip = time

}

class CheckoutViewModelFactory(
    private val tripId: Int
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CheckoutViewModel(tripId) as T
}