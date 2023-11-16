package com.example.minibus.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.City
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class LocationViewModel : ViewModel() {

    var locationUIState by mutableStateOf<MinibusUiState<List<City>>>(MinibusUiState.Loading)


    init {
        Log.d("LocationVM", "INIT")
        loadData()
    }

    fun loadData() {
        locationUIState = MinibusUiState.Loading
        viewModelScope.launch {

            locationUIState = try {

                val cityData = MinibusApi.retrofitService.getPhotos()
                MinibusUiState.Success(cityData)

            } catch (e: IOException) {

                MinibusUiState.Error(e)

            } catch (e: HttpException) {
                MinibusUiState.Error(e)
            }

        }
    }

    // fun getData(): List<City> = cityData
}