package com.example.minibus.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.R
import com.example.minibus.models.TripTime
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.TripViewModel

@Composable
fun ResultSearchScreen(uiState: State<TicketUiState>) {
    val tripViewModel: TripViewModel = viewModel()

    tripViewModel.loadData(
        uiState.value.departureCityId,
        uiState.value.arrivalCityId,
        uiState.value.departureDate.toString()
    )
    val trips = tripViewModel.getDataTrips()

    val isLoading by rememberUpdatedState(tripViewModel.isLoading)


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            TripStopsPanel()
            if (!isLoading) {
                ResultLazyColum(trips)
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(44.dp))
                }

            }
        }
    }
}

@Composable
fun ResultLazyColum(trips: List<TripTime>) = LazyColumn {
    items(trips) { item ->
        SearchItem(
            departureTime = item.departureTime.toString(),
            boardingTime = item.arrivalTime.toString(),
            numberSeats = item.numberAvailableSeats.toString(),
            fare = "BYN"
        )
    }
}

@Composable
fun TripStopsPanel() {
    StopsRow(departurePoint = "Автостанция Иваново")
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
    )
    StopsRow(departurePoint = "Автостанция Иваново")
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
fun SearchItem(departureTime: String, boardingTime: String, numberSeats: String, fare: String) {
    Card(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = numberSeats,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_medium))
                )
                Text(text = fare)
            }
        }
    }

}

@Composable
fun StopsRow(departurePoint: String) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
        Column {
            Text(text = "Место посадки")
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
            fare = "100"
        )
    }
}

@Composable
@Preview
fun ResultSearchScreenDarkPreview() {

    val viewModel: OrderViewModel = viewModel()
    MinibusTheme(useDarkTheme = true) {
        //ResultSearchScreen(viewModel)
    }
}


@Composable
@Preview
fun ResultSearchScreenLightPreview() {

    val viewModel: OrderViewModel = viewModel()
    MinibusTheme(useDarkTheme = false) {
        // ResultSearchScreen(viewModel)
    }
}