package com.example.minibus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteConfigurationScreen(
    onStartAddPassengerClicked: () -> Unit,
    onShowCalendarClick: () -> Unit,
    onDepartureSelectionClick: () -> Unit,
    onArrivalSelectionClick: () -> Unit,
    onFindTripsClick: () -> Unit,
    changeDirectionClick: () -> Unit,
    state: UseCaseState,
    selection: CalendarSelection,
    dateText: String,
    numberPassengers: Int,
    departureCity: String,
    arrivalCity: String
) {

    val imageColor = if (isSystemInDarkTheme()) {
        ColorFilter.tint(MaterialTheme.colorScheme.primary)
    } else {
        ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        //viewModel.calendarState, selection = viewModel.calendarSelection)
        val timeBoundary = LocalDate.now().let { now -> now..now.plusMonths(2) }
        CalendarDialog(
            state = state, selection = selection, CalendarConfig(
                boundary = timeBoundary,
                style = CalendarStyle.MONTH,
            )
        )

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
            Box(modifier = Modifier.fillMaxSize()) {
                Column {


                    //fromButton_1
                    DirectionButton(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        text = stringResource(R.string.from_text),
                        directionText = departureCity,
                        changeDirection = { onDepartureSelectionClick() }
                    )

                    Divider(
                        modifier = Modifier.fillMaxWidth()
                    )
                    //whereButton_2
                    DirectionButton(
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                        text = stringResource(R.string.where_text),
                        directionText = arrivalCity,
                        changeDirection = { onArrivalSelectionClick() }
                    )
                }


                Row(

                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // This pushes the button to the end
                    // Your Button code here

                    Spacer(
                        modifier = Modifier.weight(5f)
                    )
                    FilledTonalButton(
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(5.dp),
                        onClick = { changeDirectionClick() },
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),


                        ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_sync_alt_24),
                            contentDescription = null,
                            modifier = Modifier.rotate(90f),
                            colorFilter = imageColor
                        )

                    }
                    Spacer(Modifier.weight(1f))
                }


            }


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
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    title = stringResource(id = R.string.date),
                    text = dateText,
                    painter = painterResource(id = R.drawable.outline_calendar_month_24),
                    changeTipOption = onShowCalendarClick,
                    imageColor = imageColor
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
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                    title = stringResource(R.string.passengers),
                    text = if (numberPassengers == 1) "$numberPassengers человек" else "$numberPassengers человека",
                    painter = painterResource(id = R.drawable.outline_person_outline_24),
                    changeTipOption = { onStartAddPassengerClicked() },
                    imageColor = imageColor
                )
            }

            Button(
                onClick = { onFindTripsClick() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = dimensionResource(id = R.dimen.padding_medium)),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(text = stringResource(R.string.search), fontSize = 16.sp)
            }
        }

    }
}

@Composable
fun DirectionButton(
    shape: Shape,
    text: String,
    directionText: String,
    changeDirection: () -> Unit,
) {
    ElevatedButton(
        onClick = changeDirection,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                fontSize = 16.sp
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
    painter: Painter,
    changeTipOption: () -> Unit,
    imageColor: ColorFilter
) {
    Button(
        onClick = changeTipOption,
        modifier = modifier,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {


            Text(text = text, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))

            Image(painter = painter, contentDescription = "", colorFilter = imageColor)
        }
    }

}

@Composable
fun changeDirectionsButton(modifier: Modifier) {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Text(text = "ss")

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
                changeDirectionClick = {},
                selection = viewModel.calendarSelection,
                state = viewModel.calendarState,
                dateText = uiState.value.departureDate.toString(),
                numberPassengers = uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats,
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
                changeDirectionClick = {},
                selection = viewModel.calendarSelection,
                state = viewModel.calendarState,
                dateText = uiState.value.departureDate.toString(),
                numberPassengers = uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats,
                departureCity = uiState.value.departureCity,
                arrivalCity = uiState.value.arrivalCity,
            )
            //  RouteConfigurationScreen(onStartAddPassengerClicked = {})
        }
    }
}