package com.example.minibus.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.StoppingPointsViewModel
import com.example.minibus.vm.StoppingPointsViewModelFactory

@Composable
fun StoppingPointsScreen(uiState: State<TicketUiState>) {

    val stoppingPointsViewModel: StoppingPointsViewModel = viewModel(factory = StoppingPointsViewModelFactory(10))
    val cities by stoppingPointsViewModel.stoppingPointsData.collectAsState()
    val isLoading by stoppingPointsViewModel.isLoading.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        if (!isLoading){
            LazyColumn() {
                items(cities) { item ->
                    LocationItem(location = item.name, setLocationClick = {

                    })
                }
            }
        }
        else{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(44.dp))
            }
        }

    }
}



@Composable
@Preview
fun StoppingPointsDarkScreen(){
    MinibusTheme(useDarkTheme = true) {
        val viewModel: OrderViewModel = viewModel()
        val uiState = viewModel.uiState.collectAsState()
        StoppingPointsScreen(uiState)
    }
}