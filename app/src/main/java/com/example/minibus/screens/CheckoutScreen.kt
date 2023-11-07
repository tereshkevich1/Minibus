package com.example.minibus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.models.Details
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.CheckoutViewModel
import com.example.minibus.vm.CheckoutViewModelFactory
import com.example.minibus.vm.OrderViewModel


@Composable
fun CheckoutScreen(
    uiState: State<TicketUiState>,
    orderViewModel: OrderViewModel,
    navController: NavController,
    tripId: Int
) {
    val checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory(tripId))

    val details = checkoutViewModel.getData()
    val isLoading by rememberUpdatedState(checkoutViewModel.isLoading)

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!isLoading && details != null) {
            OrderPanel(uiState, orderViewModel, navController, details)
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(44.dp))
            }
        }
    }
}


@Composable
fun OrderPanel(
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
        TripStopsPanel(
            { navController.navigate("selectionDeparturePoint") },
            { navController.navigate("selectionArrivalPoint") },
            uiState
        )

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
fun Title(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        modifier = Modifier.padding(
            bottom = dimensionResource(id = R.dimen.padding_small)
        )
    )
}

@Composable
fun TransportPanel(carName: String, carColor: String, carNumber: String) {
    Row(modifier = Modifier.fillMaxWidth()) {

        /*
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .height(30.dp)
                .width(30.dp),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.baseline_directions_bus_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondaryContainer
            )
        }*/

        Column {
            Text(text = "$carColor $carName")
            Text(text = stringResource(id = R.string.car_number) + " " + carNumber)
        }

    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
    )
}

@Composable
fun InfoPanel(
    departureTime: String,
    arrivalTime: String,
    departureCity: String,
    arrivalCity: String,
    startPoint: String,
    finalPoint: String,
    departureDate: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {

        Column(modifier = Modifier.weight(0.5f)) {
            Text(text = departureTime)
            Text(text = departureDate)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = arrivalTime)
            Text(text = departureDate)
        }
        LineWithCircles(Modifier.weight(0.4f))
        Column(modifier = Modifier.weight(2f)) {
            Text(text = departureCity)
            Text(text = startPoint, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = arrivalCity)
            Text(text = finalPoint, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
    )

}

@Composable
fun LineWithCircles(modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .height(15.dp)
                .width(15.dp)
        )
        Divider(
            modifier = Modifier
                .width(4.dp)
                .weight(1f), color = MaterialTheme.colorScheme.primary
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .height(15.dp)
                .width(15.dp)
        )
    }
}

@Composable
fun PaymentPanel(numberTickets: String, fullSum: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(id = R.string.number_of_tickets) + " " + numberTickets)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "$fullSum BYN")
    }

}


@Composable
@Preview
fun CheckoutScreenDarkPreview() {
    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    MinibusTheme(useDarkTheme = true) {
        CheckoutScreen(uiState, viewModel, navController, 1)
    }
}

@Composable
@Preview
fun CheckoutScreenLightPreview() {
    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    MinibusTheme(useDarkTheme = false) {
        CheckoutScreen(uiState, viewModel, navController, 1)
    }
}