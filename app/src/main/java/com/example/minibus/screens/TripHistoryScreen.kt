package com.example.minibus.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme


@Composable
fun TripHistoryScreen() {


    Column(
        modifier = Modifier.padding(
            top = 80.dp,
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_medium)
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardTripInformation()


    }
}

@Composable
fun CardTripInformation() {

    ElevatedCard(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .fillMaxWidth()
            .height(170.dp),

        ) {
        Column(
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_medium)
            )
        ) {

            Row(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))) {
                Text(text = "Минск", fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_alt_24),
                    contentDescription = ""
                )
                Text(text = "Пинск", fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_extra_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "11:50", modifier = Modifier.weight(0.6f))

                Divider(modifier = Modifier.weight(0.5f))
                Row(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "03ч 55мин", fontSize = 10.sp)
                }

                Divider(modifier = Modifier.weight(0.5f))

                Row(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "15:00 ")
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "АВ Центральный", modifier = Modifier.weight(2f), fontSize = 10.sp)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "АВ Центральный", fontSize = 10.sp)
                }
            }
        }
    }

}



@Composable
@Preview
fun TripHistoryScreenDarkPreview() {

    MinibusTheme(useDarkTheme = true) {
        androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
            TripHistoryScreen()
        }

    }

}

@Composable
@Preview
fun TripHistoryScreenLightPreview() {
    MinibusTheme(useDarkTheme = false) {
        androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
            TripHistoryScreen()
        }
    }
}