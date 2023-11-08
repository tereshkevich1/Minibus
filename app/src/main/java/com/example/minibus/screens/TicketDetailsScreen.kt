package com.example.minibus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.minibus.R
import com.example.minibus.models.Details
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel

@Composable
fun TicketDetailsScreen(userTravelHistory: UserTravelHistory?) {

    Surface(modifier = Modifier.fillMaxSize()) {
        //if (!isLoading && details != null) {
        //TicketDetailsPanel
        //} else {
        //Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // CircularProgressIndicator(modifier = Modifier.size(44.dp))
        //  }
    }
}


@Composable
fun TicketDetailsPanel(
    uiState: State<TicketUiState>,
    orderViewModel: OrderViewModel,
    navController: NavController,
    details: Details
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
            details.time.departureTime.toString(),
            details.time.arrivalTime.toString(),
            uiState.value.departureCity,
            uiState.value.arrivalCity,
            "Станция метро 'Петровщина'",
            "Автостанция Центральная, платформа 5",
            uiState.value.departureDate.toString()
        )

        Title(stringResource(id = R.string.transport))

        TransportPanel(details.minibus.carName, details.minibus.carColor, details.minibus.carNumber)

        Title(stringResource(id = R.string.payment))

        val numberTickets = uiState.value.numberAdultsSeats + uiState.value.numberChildrenSeats
        val fullSum = (numberTickets * details.trip.price).toString()

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