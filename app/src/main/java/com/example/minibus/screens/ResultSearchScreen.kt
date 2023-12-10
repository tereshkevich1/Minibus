package com.example.minibus.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.models.TripTime
import com.example.minibus.snackbarClasses.SnackbarBottomPadding
import com.example.minibus.snackbarClasses.SnackbarDelegate
import com.example.minibus.snackbarClasses.SnackbarState
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.MinibusUiState
import com.example.minibus.vm.MyViewModelFactory
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.TripViewModel


@Composable
fun ResultSearchScreen(
    uiState: State<TicketUiState>, viewModel: OrderViewModel,
    navController: NavController, snackbarDelegate: SnackbarDelegate
) {

    val tripViewModel: TripViewModel = viewModel(
        factory = MyViewModelFactory(
            uiState.value.departureCityId,
            uiState.value.arrivalCityId,
            uiState.value.departureDate.toString()
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TripStopsPanel(
            { navController.navigate("selectionDeparturePoint") },
            { navController.navigate("selectionArrivalPoint") },
            uiState
        )
        when (tripViewModel.tripUIState) {
            is MinibusUiState.Success -> ResultLazyColum(
                (tripViewModel.tripUIState as MinibusUiState.Success<List<TripTime>>).data,
                navController,
                tripViewModel,
                uiState,
                enableIsError = { tripViewModel.enableIsError() }
            )

            is MinibusUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(44.dp))
                }
            }

            is MinibusUiState.Error -> {
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    exception = (tripViewModel.tripUIState as MinibusUiState.Error).exception,
                    tryAgainClick = {
                        tripViewModel.loadData(
                            uiState.value.departureCityId,
                            uiState.value.arrivalCityId,
                            uiState.value.departureDate.toString()
                        )
                    }
                )
            }
        }
    }


    when {
        tripViewModel.isError -> ErrorSnackbar(
            snackbarDelegate,
            disableIsError = { tripViewModel.disableIsError() })
    }


}


@Composable
fun ResultLazyColum(
    trips: List<TripTime>,
    navController: NavController,
    tripViewModel: TripViewModel,
    uiState: State<TicketUiState>,
    enableIsError: () -> Unit
) = LazyColumn {
    items(trips) { item ->
        SearchItem(
            departureTime = item.departureTime.toString(),
            boardingTime = item.arrivalTime.toString(),
            price = item.price.toString(),
            numberSeats =
            tripViewModel.printNumberSeats(item.numberAvailableSeats),
            colorSeats = tripViewModel.printColorSeats(item.numberAvailableSeats),
            fare = "BYN",
            checkoutClick = {
                Log.d("ResultLazyColum", "Navigating to checkoutScreen with trip ID: ${item.id}")
                if (item.numberAvailableSeats >= uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats) {
                    navController.navigate("checkoutScreen/${item.id}")
                } else if (!tripViewModel.isError) {
                    enableIsError()
                }
            },
            enabled = item.numberAvailableSeats > 0

        )
    }
}

@Composable
fun ErrorSnackbar(snackbarDelegate: SnackbarDelegate, disableIsError: () -> Unit) {

    LaunchedEffect(key1 = null) {
        snackbarDelegate.closeSnackbar()
        snackbarDelegate.showSnackbar(
            SnackbarState.ERROR,
            message= "Места заняты, вас много",
            snackbarBottomPadding = SnackbarBottomPadding.DEFAULT
        )
        disableIsError()
    }

}

@Composable
fun TripStopsPanel(
    changeDeparturePoint: () -> Unit,
    changeArrivalPoint: () -> Unit,
    uiState: State<TicketUiState>
) {
    StopsRow(
        departurePoint = uiState.value.departurePoint.toString(),
        title = stringResource(R.string.start_point),
        changeStopPoint = { changeDeparturePoint() })
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
    )
    StopsRow(departurePoint = uiState.value.arrivalPoint.toString(),
        title = stringResource(R.string.final_point),
        changeStopPoint = { changeArrivalPoint() })
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItem(
    departureTime: String,
    boardingTime: String,
    price: String,
    numberSeats: String,
    fare: String,
    colorSeats: Color,
    checkoutClick: () -> Unit,
    enabled: Boolean
) {
    Card(onClick = checkoutClick, modifier = Modifier.fillMaxWidth(), enabled = enabled) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = departureTime)
                Text(
                    text = "-",
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.padding_extra_small),
                        end = dimensionResource(id = R.dimen.padding_extra_small)
                    )
                )
                Text(text = boardingTime)
                Spacer(modifier = Modifier.weight(0.8f))
                Text(
                    text = numberSeats,
                    color = colorSeats,
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_large_medium))
                )
                Text(
                    text = price,
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_less_medium))
                )
                Text(text = fare)
            }
        }
    }

}

@Composable
fun StopsRow(departurePoint: String, title: String, changeStopPoint: () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = changeStopPoint)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = title)
            Text(text = departurePoint, fontSize = 12.sp)

        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.baseline_expand_more_24),
            contentDescription = null
        )
    }

}

@Composable
@Preview
fun ResultSearchItemScreenDarkPreview() {

    MinibusTheme(useDarkTheme = true) {
        SearchItem(
            departureTime = "22-22",
            boardingTime = "00:00",
            numberSeats = "10",
            fare = "100",
            price = "27",
            colorSeats = Color.Green,
            checkoutClick = {},
            enabled = true
        )
    }
}

@Composable
@Preview
fun ResultSearchScreenDarkPreview() {

    val viewModel: OrderViewModel = viewModel()
    val uiState: State<TicketUiState> = viewModel.uiState.collectAsState()
    MinibusTheme(useDarkTheme = true) {
        // ResultSearchScreen(uiState, {}, {},{})
    }
}


@Composable
@Preview
fun ResultSearchScreenLightPreview() {

    val viewModel: OrderViewModel = viewModel()
    val uiState: State<TicketUiState> = viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    MinibusTheme(useDarkTheme = false) {
        ResultSearchScreen(uiState, viewModel, navController, snackbarDelegate = SnackbarDelegate())
    }
}