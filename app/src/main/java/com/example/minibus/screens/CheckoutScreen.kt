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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel


@Composable
fun CheckoutScreen(viewModel: OrderViewModel) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .verticalScroll(scrollState)
        ) {
            TripStopsPanel(
                {},
                {}
            )

            Title("Маршрут")

            InfoPanel(
                "20:30",
                "23:50",
                "Минск",
                "Иваново",
                "Станция метро 'Петровщина'",
                "Автостанция Центральная, платформа 5",
                "28 окт"
            )

            Title("Транспорт")

            TransportPanel("Mercedes Sprinter", "Зеленый", "AB 124-23")

            Title("Оплата")

            PaymentPanel("2","50")

            Spacer(modifier = Modifier.weight(1f))

            ElevatedButton(
                onClick = { }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Оформить заказ")
            }


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
            Text(text = "Номер: $carNumber")
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
fun PaymentPanel(numberTickets: String, fullSum: String){
    Row (modifier = Modifier.fillMaxWidth()){
        Text(text = "Количество билетов: $numberTickets")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "$fullSum BYN")
    }

}


@Composable
@Preview
fun DarkInfoPanelPreview() {
    MinibusTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {


            InfoPanel(
                "20:30",
                "00:50",
                "Минск",
                "Иваново",
                "метро Петровщина",
                "Автостанция Центральная, платформа 5",
                "28 окт"
            )
        }
    }
}

@Composable
@Preview
fun CheckoutScreenDarkPreview() {
    val viewModel: OrderViewModel = viewModel()
    MinibusTheme(useDarkTheme = true) {
        CheckoutScreen(viewModel)
    }
}