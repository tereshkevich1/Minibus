package com.example.minibus.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.StopPoint
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StoppingPointsViewModel(val cityId: Int, val departurePoint: Boolean) : ViewModel() {

    var stoppingPointsUIState by mutableStateOf<MinibusUiState<List<StopPoint>>>(MinibusUiState.Loading)

    init {
        loadData()
    }

    fun loadData() {
        // Simulate loading data from a database
        viewModelScope.launch {
            stoppingPointsUIState = MinibusUiState.Loading

            stoppingPointsUIState = try {
                val stoppingPointsData = MinibusApi.retrofitService.getStopsByCityId(cityId)
                MinibusUiState.Success(stoppingPointsData)
            } catch (e: IOException) {
                MinibusUiState.Error(e)
            } catch (e: HttpException) {
                MinibusUiState.Error(e)
            }
        }
    }
}

class StoppingPointsViewModelFactory(private val cityId: Int, private val departurePoint: Boolean) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StoppingPointsViewModel(cityId, departurePoint) as T
}

