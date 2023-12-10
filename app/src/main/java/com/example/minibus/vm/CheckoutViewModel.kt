package com.example.minibus.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.Details
import com.example.minibus.network.MinibusApi
import com.example.minibus.state_models.ButtonUiState
import com.example.minibus.state_models.ButtonUiState.Defolt
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CheckoutViewModel(tripId: Int) : ViewModel() {
    private var orderDetailsObject: Details? by mutableStateOf(null)

    var isLoading by mutableStateOf(true)

    var buttonState: ButtonUiState by mutableStateOf(Defolt)

    var errorMessage by mutableStateOf("")


    init {
        viewModelScope.launch {
            orderDetailsObject = MinibusApi.retrofitService.getOrderDetails(tripId)
            isLoading = false
        }
    }

    var showSnackbar by mutableStateOf(false)

    fun disableSnackbarState() {
        showSnackbar = false
    }

    fun addOrder(
        tripId: Int,
        userId: Int,
        numberTickets: Int,
        departureStopId: Int,
        arrivalStopId: Int
    ) {
        Log.d("addOrderParameters", " TripId: $tripId,userId: $userId")
        buttonState = ButtonUiState.Loading
        if (departureStopId == 0 || arrivalStopId == 0) {
            buttonState = ButtonUiState.Error
            errorMessage = "Выберите пункты посадки/высадки"
            showSnackbar = true
        } else {
            viewModelScope.launch {
                buttonState = try {
                    MinibusApi.retrofitService.addOrder(
                        userId,
                        tripId,
                        numberTickets,
                        1,
                        departureStopId,
                        arrivalStopId
                    )
                    ButtonUiState.Success

                } catch (e: IOException) {
                    showSnackbar = true
                    errorMessage = "чет то"
                    Log.d("addOrder ERROR", "$e")
                    ButtonUiState.Error

                } catch (e: HttpException) {
                    showSnackbar = true
                    errorMessage = "чет то"
                    Log.d("addOrder ERROR", "$e")
                    ButtonUiState.Error

                }
            }
        }
    }

    fun getData(): Details? = orderDetailsObject

}

class CheckoutViewModelFactory(
    private val tripId: Int
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CheckoutViewModel(tripId) as T
}