package com.example.minibus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteConfigurationScreen(
    onStartAddPassengerClicked: () -> Unit,
    onShowCalendarClick: () -> Unit,
    onDepartureSelectionClick: () -> Unit,
    onArrivalSelectionClick: () -> Unit,
    onFindTripsClick: () -> Unit,
    state: UseCaseState,
    selection: CalendarSelection,
    dateText: String,
    numberPassengersText: String,
    departureCity: String,
    arrivalCity: String
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        //viewModel.calendarState, selection = viewModel.calendarSelection)
        CalendarDialog(state = state, selection = selection)
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.padding_medium),
                    bottom = dimensionResource(R.dimen.padding_medium),
                    start = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium)

                )
                .verticalScroll(scrollState)
        ) {

            //fromButton_1
            DirectionButton(
                shape = MaterialTheme.shapes.large,
                text = stringResource(R.string.from_text),
                directionText = departureCity,
                changeDirection = { onDepartureSelectionClick() })

            Divider(
                modifier = Modifier.fillMaxWidth()
            )

            //whereButton_2
            DirectionButton(
                shape = MaterialTheme.shapes.small,
                text = stringResource(R.string.where_text),
                directionText = arrivalCity,
                changeDirection = { onArrivalSelectionClick() })


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small)
                    )
            ) {

                //ChangeDateButton
                TripOptionsButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    shape = MaterialTheme.shapes.extraSmall,
                    title = stringResource(id = R.string.date),
                    text = dateText,
                    imageVector = Icons.Filled.DateRange,
                    changeTipOption = onShowCalendarClick
                )

                Divider(
                    modifier = Modifier
                        .height(64.dp)
                        .width(1.dp)
                )

                //AddPassengersButton
                TripOptionsButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    shape = MaterialTheme.shapes.extraLarge,
                    title = stringResource(R.string.passengers),
                    text = "$numberPassengersText человек",
                    imageVector = Icons.Filled.Person,
                    changeTipOption = { onStartAddPassengerClicked() }
                )
            }

            // add checkedValue in viewModel.uiState
            var checkedValue by rememberSaveable {
                mutableStateOf(true)
            }
            Row(verticalAlignment = CenterVertically) {
                Checkbox(checked = checkedValue, onCheckedChange = { checkedValue = it })
                Text("Отправить посылку")
            }

            ElevatedButton(
                onClick = { onFindTripsClick() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(text = stringResource(R.string.search))
            }
        }

    }
}


@Composable
fun DirectionButton(
    shape: Shape,
    text: String,
    directionText: String,
    changeDirection: () -> Unit
) {
    ElevatedButton(
        onClick = changeDirection,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = text,
                modifier = Modifier,
            )
            Text(
                text = directionText,
                modifier = Modifier,
            )
        }
    }
}


@Composable
fun TripOptionsButton(
    modifier: Modifier,
    shape: Shape,
    text: String,
    title: String,
    imageVector: ImageVector,
    changeTipOption: () -> Unit
) {
    Button(
        onClick = changeTipOption,
        modifier = modifier,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = title,
                modifier = Modifier,
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = text)
                Spacer(modifier = Modifier.weight(1f))
                Image(imageVector = imageVector, contentDescription = "")
            }
        }
    }

}

@Composable
@Preview
fun RouteConfigurationScreenLightThemePreview() {
    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    MinibusTheme(useDarkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            RouteConfigurationScreen(
                onStartAddPassengerClicked = { },
                onShowCalendarClick = { viewModel.showCalendar() },
                onDepartureSelectionClick = {},
                onArrivalSelectionClick = {},
                onFindTripsClick = {},
                selection = viewModel.calendarSelection,
                state = viewModel.calendarState,
                dateText = uiState.value.departureDate.toString(),
                numberPassengersText = (uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats).toString(),
                departureCity = uiState.value.departureCity,
                arrivalCity = uiState.value.arrivalCity
            )
        }
    }
}

@Composable
@Preview
fun RouteConfigurationScreenDarkThemePreview() {
    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    MinibusTheme(useDarkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            RouteConfigurationScreen(
                onStartAddPassengerClicked = { },
                onShowCalendarClick = { viewModel.showCalendar() },
                onDepartureSelectionClick = {},
                onArrivalSelectionClick = {},
                onFindTripsClick = {},
                selection = viewModel.calendarSelection,
                state = viewModel.calendarState,
                dateText = uiState.value.departureDate.toString(),
                numberPassengersText = (uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats).toString(),
                departureCity = uiState.value.departureCity,
                arrivalCity = uiState.value.arrivalCity,
            )
            //  RouteConfigurationScreen(onStartAddPassengerClicked = {})
        }
    }
}