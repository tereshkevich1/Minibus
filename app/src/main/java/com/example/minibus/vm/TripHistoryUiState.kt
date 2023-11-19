package com.example.minibus.vm

sealed class TripHistoryUiState<out T> {
    object Loading : TripHistoryUiState<Nothing>()
    class Success<T>(val futureTrips: T, val pastTrips: T) : TripHistoryUiState<T>()
    class Error(val exception: Exception) : TripHistoryUiState<Nothing>()
}