package com.example.minibus.screens

import Order
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.models.RoutD
import com.example.minibus.models.Time
import com.example.minibus.models.Transport
import com.example.minibus.models.Trip
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.TicketDetailsViewModel
import com.example.minibus.vm.TicketDetailsViewModelFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                    TicketDetailsPanel(navController, userTravelHistory, transport, ticketViewModel)
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
    transport: Transport,
    ticketViewModel: TicketDetailsViewModel
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(scrollState)
    ) {

        Title(stringResource(id = R.string.route))
        val dateTimeFormat = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
        InfoPanel(
            userTravelHistory.time.departureTime.toString(),
            userTravelHistory.time.arrivalTime.toString(),
            userTravelHistory.departureCity,
            userTravelHistory.arrivalCity,
            userTravelHistory.departurePoint,
            userTravelHistory.arrivalPoint,
            userTravelHistory.trip.departureDate.format(dateTimeFormat).toString()
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

        val openDialog = remember { mutableStateOf(false) }

        ElevatedButton(
            onClick = { openDialog.value = true }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = dimensionResource(id = R.dimen.padding_small)),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.errorContainer,
                containerColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(text = stringResource(id = R.string.cancel_order))
        }

        when {
            openDialog.value -> {
                ConfirmationDialog(
                    onDismissRequest = { openDialog.value = false },
                    onConfirmation = {
                        openDialog.value = false
                        ticketViewModel.deleteOrder(userTravelHistory.order.id)
                        navController.navigate("Story") {
                            popUpTo("Search") {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }

                    },
                    dialogText = stringResource(R.string.cancel_order_ask),
                    dialogTitle = stringResource(R.string.cancel_order_alert_dialog)
                )
            }
        }
    }

}

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(
                    text = stringResource(id = R.string.cancel_order),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    )
}


@Composable
@Preview
fun TicketDetailsScreenDarkPreview() {
    val navController = rememberNavController()
    val order = Order(1, 2, 1, 1, 1, 1, 1)
    val route = RoutD(1, 1, 1, 100)
    val time = Time(1, LocalTime.MIDNIGHT, LocalTime.NOON)
    val trip = Trip(1, 1, 1, 1, 1, 1, 1, LocalDate.now())
    val userTravelHistory = UserTravelHistory(
        order,
        trip,
        time,
        route,
        "Минск",
        "Иваново",
        "ост м Петровщина",
        "Автостанция центральняа"
    )
    MinibusTheme(useDarkTheme = true) {
        TicketDetailsScreen(userTravelHistory, navController)
    }
}

@Composable
@Preview
fun TicketDetailsScreenLightPreview() {
    MinibusTheme(useDarkTheme = false) {
        //TicketDetailsScreen()
    }
}