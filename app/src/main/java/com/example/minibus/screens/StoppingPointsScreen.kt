package com.example.minibus.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.models.StopPoint
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.MinibusUiState
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.StoppingPointsViewModel
import com.example.minibus.vm.StoppingPointsViewModelFactory

@Composable
fun StoppingPointsScreen(
    uiState: State<TicketUiState>,
    departurePoint: Boolean,
    viewModel: OrderViewModel
) {

    val stoppingPointsViewModel: StoppingPointsViewModel = if (departurePoint) {
        viewModel(
            factory = StoppingPointsViewModelFactory(
                uiState.value.departureCityId,
                departurePoint
            )
        )
    } else {
        viewModel(
            factory = StoppingPointsViewModelFactory(
                uiState.value.arrivalCityId,
                departurePoint
            )
        )
    }

    when (stoppingPointsViewModel.stoppingPointsUIState) {
        is MinibusUiState.Success -> LazyColumn {
            items((stoppingPointsViewModel.stoppingPointsUIState as MinibusUiState.Success<List<StopPoint>>).data) { item ->
                LocationItem(
                    location = item.name,
                    setLocationClick = { viewModel.changeStoppingPoint(departurePoint, item) }
                )
            }
        }

        is MinibusUiState.Error -> Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ErrorScreen(
                exception = (stoppingPointsViewModel.stoppingPointsUIState as MinibusUiState.Error).exception,
                tryAgainClick = { stoppingPointsViewModel.loadData() })
        }

        is MinibusUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(44.dp))
        }
    }
}


@Composable
@Preview
fun StoppingPointsDarkScreen() {
    MinibusTheme(useDarkTheme = true) {
        val viewModel: OrderViewModel = viewModel()
        val uiState = viewModel.uiState.collectAsState()
        StoppingPointsScreen(uiState, true, viewModel)
    }
}