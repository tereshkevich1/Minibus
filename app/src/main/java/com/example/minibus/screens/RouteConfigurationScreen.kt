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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.minibus.R

import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel


@Composable
fun RouteConfigurationScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(
                top = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium)

            )
        ) {

            //fromButton_1
            DirectionButton(
                shape = MaterialTheme.shapes.large,
                text = stringResource(R.string.from_text),
                changeDirection = {})

            Divider(
                modifier = Modifier.fillMaxWidth()
            )

            //whereButton_2
            DirectionButton(
                shape = MaterialTheme.shapes.small,
                text = stringResource(R.string.where_text),
                changeDirection = {})


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
                    title = "*",
                    text = "25 september",
                    imageVector = Icons.Filled.DateRange,
                    changeTipOption = {}
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
                    title = stringResource(R.string.where_text),
                    text = "25 september",
                    imageVector = Icons.Filled.Person,
                    changeTipOption = {}
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
                onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(text = "Find")
            }
        }

    }
}


@Composable
fun DirectionButton(shape: Shape, text: String, changeDirection: () -> Unit) {
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
    MinibusTheme(useDarkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            RouteConfigurationScreen()
        }
    }
}

@Composable
@Preview
fun RouteConfigurationScreenDarkThemePreview() {
    MinibusTheme(useDarkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            RouteConfigurationScreen()
        }
    }
}