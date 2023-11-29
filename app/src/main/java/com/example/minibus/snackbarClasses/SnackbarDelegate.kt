package com.example.minibus.snackbarClasses

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class SnackbarState {
    DEFAULT,
    ERROR
}

class SnackbarDelegate(
    private var snackbarHostState: SnackbarHostState? = null,
    private var coroutineScope: CoroutineScope? = null
) {

    private var snackbarState: SnackbarState = SnackbarState.DEFAULT

    val snackbarBackgroundColor: Color
        @Composable
        get() = when (snackbarState) {
            SnackbarState.DEFAULT -> SnackbarDefaults.color
            SnackbarState.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        }

    fun showSnackbar(
        state: SnackbarState,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        this.snackbarState = state
        coroutineScope?.launch {
            snackbarHostState?.showSnackbar(message, actionLabel, duration = duration)
        }
    }

    fun closeSnackbar(){
        coroutineScope?.launch {
            snackbarHostState?.currentSnackbarData?.dismiss()
        }
    }
}