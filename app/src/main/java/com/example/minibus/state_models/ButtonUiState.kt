package com.example.minibus.state_models

sealed interface ButtonUiState {
    object Success : ButtonUiState
    object Error : ButtonUiState
    object Loading : ButtonUiState
    object Defolt : ButtonUiState
}