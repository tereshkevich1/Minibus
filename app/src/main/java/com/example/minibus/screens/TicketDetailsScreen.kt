package com.example.minibus.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.minibus.R
import com.example.minibus.models.Transport
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.TicketDetailsViewModel
import com.example.minibus.vm.TicketDetailsViewModelFactory

@Composable
fun TicketDetailsScreen(userTravelHistory: UserTravelHistory?, navController: NavController) {

    if (userTravelHistory != null) {
        val ticketViewModel: TicketDetailsViewModel =
            viewModel(factory = TicketDetailsViewModelFactory(userTravelHistory.trip.minibusId))
        val transportState = ticketViewModel.bus.collectAsState()
        val isLoading by ticketViewModel.isLoading.collectAsState()

        Surface(modifier = Modifier.fillMaxSize()) {
            if (!isLoading) {
                transportState.value?.let { transport ->
                    TicketDetailsPanel(navController, userTravelHistory, transport)
                } ?: run {
                    // Обработка случая, когда transport == null
                    Text("Transport details are unavailable.")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(44.dp))
                }
            }
        }
    } else {
        // Обработка случая, когда userTravelHistory == null
        Text("No travel history available.")
    }


}


@Composable
fun TicketDetailsPanel(
    navController: NavController,
    userTravelHistory: UserTravelHistory,
    transport: Transport
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(scrollState)
    ) {

        Title(stringResource(id = R.string.route))

        InfoPanel(
            userTravelHistory.time.departureTime.toString(),
            userTravelHistory.time.arrivalTime.toString(),
            userTravelHistory.departureCity,
            userTravelHistory.arrivalCity,
            "Станция метро 'Петровщина'",
            "Автостанция Центральная, платформа 5",
            userTravelHistory.trip.departureDate.toString()
        )

        Title(stringResource(id = R.string.transport))

        TransportPanel(
            transport.car.brandModel,
            transport.bus.carColor,
            transport.bus.carNumber
        )

        Title(stringResource(id = R.string.payment))

        val numberTickets = userTravelHistory.order.numberTickets
        val fullSum = (numberTickets * userTravelHistory.trip.price).toString()

        PaymentPanel(numberTickets.toString(), fullSum)

        Spacer(modifier = Modifier.weight(1f))

        ElevatedButton(
            onClick = { }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(text = stringResource(id = R.string.checkout))
        }

    }

}


@Composable
@Preview
fun TicketDetailsScreenDarkPreview() {
    MinibusTheme(useDarkTheme = true) {
        //TicketDetailsScreen()
    }
}

@Composable
@Preview
fun TicketDetailsScreenLightPreview() {
    MinibusTheme(useDarkTheme = false) {
        //TicketDetailsScreen()
    }
}