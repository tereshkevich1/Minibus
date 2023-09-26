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
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minibus.R

import com.example.minibus.ui.theme.MinibusTheme


@Composable
fun RouteConfigurationScreen() {

    Column(
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_medium),
            start = dimensionResource(R.dimen.padding_small),
            end = dimensionResource(R.dimen.padding_small)

        )
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = MaterialTheme.shapes.medium,
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
                    text = stringResource(R.string.from_text),
                    modifier = Modifier,
                )
            }
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = MaterialTheme.shapes.small,
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
                    text = stringResource(R.string.where_text),
                    modifier = Modifier,
                )
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

            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = MaterialTheme.shapes.extraSmall,
                elevation = ButtonDefaults.buttonElevation(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.where_text),
                        modifier = Modifier,
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "25 september")
                        Spacer(modifier = Modifier.weight(1f))
                        Image(imageVector = Icons.Filled.DateRange, contentDescription = "")
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .height(64.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = ButtonDefaults.buttonElevation(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.secondaryContainer
                )

            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.where_text),
                        modifier = Modifier,
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "25 september")
                        Spacer(modifier = Modifier.weight(1f))
                        Image(imageVector = Icons.Filled.Person, contentDescription = "")
                    }
                }
            }
        }

        Button(
            onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            elevation = ButtonDefaults.buttonElevation(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.primaryContainer
            )

        ) {
            Text(text = "Find")
        }
    }

}

@Composable
@Preview
fun RouteConfigurationScreenPreview() {
    MinibusTheme {
        RouteConfigurationScreen()
    }
}