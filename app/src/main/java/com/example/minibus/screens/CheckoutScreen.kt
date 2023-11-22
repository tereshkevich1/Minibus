package com.example.minibus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.models.Bus
import com.example.minibus.models.Car
import com.example.minibus.models.Details
import com.example.minibus.models.Time
import com.example.minibus.models.Trip
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.CheckoutViewModel
import com.example.minibus.vm.CheckoutViewModelFactory
import com.example.minibus.vm.OrderViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


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

    var openDialog by remember { mutableStateOf(false) }

    when {
        openDialog -> {
            successfulOrderAlertDialog(onDismissRequest = { openDialog = false },
                onConfirmation = { openDialog = false },
                onDismissButtonClick = { openDialog = false })
        }

    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!isLoading && details != null) {
            OrderPanel(
                uiState,
                orderViewModel,
                navController,
                details,
                onOrderButtonClick = { openDialog = true })
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
    details: Details,
    onOrderButtonClick: () -> Unit
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

        val outputFormat = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
        val formatDate = uiState.value.departureDate.format(outputFormat).toString()

        InfoPanel(
            details.time.departureTime.toString(),
            details.time.arrivalTime.toString(),
            uiState.value.departureCity,
            uiState.value.arrivalCity,
            uiState.value.departurePoint ?: "",
            uiState.value.arrivalPoint ?: "",
            formatDate
        )

        Title(stringResource(id = R.string.transport))

        TransportPanel(
            details.busInfo.brandModel, details.minibus.carColor, details.minibus.carNumber
        )

        Title(stringResource(id = R.string.payment))

        val numberTickets = uiState.value.numberAdultsSeats + uiState.value.numberChildrenSeats
        val fullSum = (numberTickets * details.trip.price).toString()

        PaymentPanel(numberTickets.toString(), fullSum)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onOrderButtonClick() },
            modifier = Modifier
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
        text = title, fontSize = 18.sp, modifier = Modifier.padding(
            bottom = dimensionResource(id = R.dimen.padding_small)
        ), fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun TransportPanel(carName: String, carColor: String, carNumber: String) {
    Row(modifier = Modifier.fillMaxWidth()) {

        Column {
            Text(text = "$carColor $carName", maxLines = 1, fontSize = 14.sp)
            Text(
                text = stringResource(id = R.string.car_number) + " " + carNumber, fontSize = 14.sp
            )
        }

    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
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
            .height(100.dp)
    ) {

        Column(modifier = Modifier.weight(0.7f)) {
            Text(text = departureTime)
            Text(text = departureDate, fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = arrivalTime)
            Text(text = departureDate, fontSize = 12.sp)
        }
        LineWithCircles(Modifier.weight(0.4f))
        Column(modifier = Modifier.weight(2f)) {
            Text(text = departureCity)
            Text(
                text = startPoint, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.37f))
            Text(text = arrivalCity)
            Text(
                text = finalPoint, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp
            )
        }

    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
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
                .weight(0.6f), color = MaterialTheme.colorScheme.primary
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
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        Text(
            text = stringResource(id = R.string.number_of_tickets) + " " + numberTickets,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "$fullSum BYN")
    }

}

@Composable
fun successfulOrderAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    val successColor: Color = MaterialTheme.colorScheme.primary
    val textButtonColor: Color = MaterialTheme.colorScheme.outline
    AlertDialog(

        shape = RoundedCornerShape(10.dp),
        modifier = Modifier,
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(
                    text = stringResource(id = R.string.agreement),
                    color = textButtonColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissButtonClick() }) {
                Text(
                    text = stringResource(id = R.string.refusal),
                    color = textButtonColor
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = stringResource(id = R.string.success_order), modifier = Modifier.padding(
                        end = dimensionResource(
                            id = R.dimen.padding_medium
                        )
                    ),
                    color = successColor
                )


                Icon(
                    painter = painterResource(id = R.drawable.outline_check_circle_outline_24),
                    contentDescription = null,
                    Modifier.size(30.dp),
                    tint = successColor
                )
            }

        },
        text = { Text(text = stringResource(id = R.string.return_trip)) })
}


@Composable
@Preview
fun AlertDialogPreview() {
    MinibusTheme(useDarkTheme = true) {
        successfulOrderAlertDialog(onDismissRequest = { },
            onConfirmation = { },
            onDismissButtonClick = { })
    }
}

@Composable
@Preview
fun CheckoutScreenDarkPreview() {
    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val bus = Bus(1, "AB-1234", 1, 2009, "Зеленый")
    val time = Time(1, LocalTime.MIDNIGHT, LocalTime.NOON)
    val car = Car(1, "Mercedes Sprinter", 15)
    val trip = Trip(1, 1, 1, 1, 1, 1, 1, LocalDate.now())
    val details = Details(trip, bus, time, car)
    MinibusTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OrderPanel(uiState, viewModel, navController, details, {})
        }

    }
}

@Composable
@Preview
fun CheckoutScreenLightPreview() {
    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val bus = Bus(1, "AB-1234", 1, 2009, "Зеленый")
    val time = Time(1, LocalTime.MIDNIGHT, LocalTime.NOON)
    val car = Car(1, "Mercedes Sprinter", 15)
    val trip = Trip(1, 1, 1, 1, 1, 1, 1, LocalDate.now())
    val details = Details(trip, bus, time, car)
    MinibusTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OrderPanel(uiState, viewModel, navController, details, {})
        }

    }
}