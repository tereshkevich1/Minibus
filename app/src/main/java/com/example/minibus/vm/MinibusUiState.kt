package com.example.minibus.vm

sealed class MinibusUiState<out T> {
    object Loading : MinibusUiState<Nothing>()
    class Success<T>(val data: T) : MinibusUiState<T>()
    class Error(val exception: Exception) : MinibusUiState<Nothing>()
}