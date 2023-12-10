package com.example.minibus.snackbarClasses

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minibus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class SnackbarState {
    DEFAULT,
    ERROR
}

enum class SnackbarBottomPadding {
    DEFAULT,
    ELEVATED
}

class SnackbarDelegate(
    private var snackbarHostState: SnackbarHostState? = null,
    private var coroutineScope: CoroutineScope? = null
) {

    private var snackbarState: SnackbarState = SnackbarState.DEFAULT
    private var snackbarBottomPaddingState: SnackbarBottomPadding = SnackbarBottomPadding.DEFAULT

    val snackbarBackgroundColor: Color
        @Composable
        get() = when (snackbarState) {
            SnackbarState.DEFAULT -> SnackbarDefaults.color
            SnackbarState.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        }
    val snackbarBottomPadding: Dp
        @Composable
        get() = when (snackbarBottomPaddingState) {
            SnackbarBottomPadding.DEFAULT -> dimensionResource(id = R.dimen.padding_large_medium)
            SnackbarBottomPadding.ELEVATED -> 82.dp
        }

    fun showSnackbar(
        state: SnackbarState,
        snackbarBottomPadding: SnackbarBottomPadding,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        this.snackbarState = state
        this.snackbarBottomPaddingState = snackbarBottomPadding
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