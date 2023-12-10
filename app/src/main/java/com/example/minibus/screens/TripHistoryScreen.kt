package com.example.minibus.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.models.Time
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.network.JsonFormat
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.HistoryViewModel
import com.example.minibus.vm.TripHistoryUiState
import kotlinx.serialization.encodeToString
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun TripHistoryScreen(navController: NavController) {

    val historyViewModel: HistoryViewModel = viewModel()
    var firstButtonActive by rememberSaveable { mutableStateOf(true) }

    Column {
        SwitchPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.padding_extra_small),
                    end = dimensionResource(id = R.dimen.padding_extra_small),
                    bottom = dimensionResource(id = R.dimen.padding_large_medium)
                ),
            firstButtonActive,
            changeActiveFutureButton = { firstButtonActive = true },
            changeActiveArchiveButton = { firstButtonActive = false }
        )

        when (historyViewModel.tripHistoryUIState) {
            is TripHistoryUiState.Success -> if (firstButtonActive) {
                InformationLazyColumn(
                    (historyViewModel.tripHistoryUIState as TripHistoryUiState.Success<List<UserTravelHistory>>).futureTrips,
                    historyViewModel,
                    navController,
                    firstButtonActive
                )
            } else {
                InformationLazyColumn(
                    (historyViewModel.tripHistoryUIState as TripHistoryUiState.Success<List<UserTravelHistory>>).pastTrips,
                    historyViewModel,
                    navController,
                    firstButtonActive
                )
            }

            is TripHistoryUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(44.dp))
            }

            is TripHistoryUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = dimensionResource(id = R.dimen.padding_medium),
                                end = dimensionResource(id = R.dimen.padding_medium)
                            ),
                        (historyViewModel.tripHistoryUIState as TripHistoryUiState.Error).exception,
                        tryAgainClick = { historyViewModel.loadUserTravelHistoryList() }
                    )
                }
            }
        }
    }
}


@Composable
fun InformationLazyColumn(
    userTravelHistoryList: List<UserTravelHistory>,
    historyViewModel: HistoryViewModel, navController: NavController,
    firstButtonActive: Boolean
) {
    LazyColumn(
        modifier = Modifier.padding(
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_medium)
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(userTravelHistoryList) { item ->
            CardTripInformation(
                item.departureCity,
                item.arrivalCity,
                item.time,
                item.departurePoint,
                item.arrivalPoint,
                item.trip.departureDate,
                item.trip.price,
                item.order.numberTickets,
                historyViewModel,
                moreInfoClick =
                {

                    Log.d("TripHistoryClick", "work!")
                    val json = Uri.encode(JsonFormat.instance.encodeToString(item))
                    Log.d("TripHistoryClick", "wo $json")
                    navController.navigate("detailsScreen/$json")

                },
                firstButtonActive
            )
        }
    }
}


@Composable
fun CardTripInformation(
    departureCity: String, arrivalCity: String,
    time: Time, departurePoint: String, arrivalPoint: String,
    departureDate: LocalDate, price: Int, numberSeats: Int,
    historyViewModel: HistoryViewModel, moreInfoClick: () -> Unit,
    firstButtonActive: Boolean
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .clip(MaterialTheme.shapes.medium)
            .then(if (firstButtonActive) Modifier.clickable { moreInfoClick() } else Modifier),

        ) {
        Column(
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_less_medium),
                end = dimensionResource(id = R.dimen.padding_less_medium),
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_medium)
            )
        ) {

            DirectionRow(departureCity, arrivalCity)

            TimeRow(time, historyViewModel)

            LocationRow(departurePoint, arrivalPoint)

            AdditionalInformationRow(departureDate, price, numberSeats)
        }
    }

}

@Composable
fun CardTripOptionInformation(imageVector: Painter, text: String) {
    Card(
        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_extra_small),
                bottom = dimensionResource(id = R.dimen.padding_extra_small),
                end = dimensionResource(id = R.dimen.padding_small),
                start = dimensionResource(id = R.dimen.padding_small)

            )
        ) {
            Icon(
                painter = imageVector, contentDescription = null, modifier = Modifier.padding(
                    end = dimensionResource(
                        id = R.dimen.padding_extra_small
                    )
                )
            )
            Text(text = text)
        }
    }
}

@Composable
fun DirectionRow(departureCity: String, arrivalCity: String) {
    Row(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))) {
        Text(text = departureCity, fontWeight = FontWeight.Bold)
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_right_alt_24),
            contentDescription = ""
        )
        Text(text = arrivalCity, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TimeRow(time: Time, historyViewModel: HistoryViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.padding_extra_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = time.departureTime.toString(), modifier = Modifier.weight(0.6f))

        Divider(
            modifier = Modifier.weight(0.5f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {


            Text(
                text = historyViewModel.setDuration(time.departureTime, time.arrivalTime),
                fontSize = 10.sp
            )
        }

        Divider(
            modifier = Modifier.weight(0.5f),
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Text(text = time.arrivalTime.toString())
        }

    }
}

@Composable
fun LocationRow(departurePoint: String, arrivalPoint: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_extra_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = departurePoint,
            modifier = Modifier.weight(2f),
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = arrivalPoint,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AdditionalInformationRow(departureDate: LocalDate, price: Int, numberSeats: Int) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(top = dimensionResource(id = R.dimen.padding_extra_small))
    ) {
        CardTripOptionInformation(
            painterResource(id = R.drawable.baseline_calendar_month_24),
            departureDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
        )
        CardTripOptionInformation(
            painterResource(id = R.drawable.baseline_monetization_on_24),
            "$price BYN"
        )
        CardTripOptionInformation(
            painterResource(id = R.drawable.baseline_person_24),
            "$numberSeats место"
        )
    }
}

@Composable
@Preview
fun TripHistoryScreenDarkPreview() {
    val navController = rememberNavController()

    MinibusTheme(useDarkTheme = true) {
        androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
            TripHistoryScreen(navController)
        }

    }

}

@Composable
@Preview
fun TripHistoryScreenLightPreview() {
    val navController = rememberNavController()
    MinibusTheme(useDarkTheme = false) {
        androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
            TripHistoryScreen(navController)
        }
    }
}