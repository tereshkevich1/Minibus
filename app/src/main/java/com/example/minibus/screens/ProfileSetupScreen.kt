package com.example.minibus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R

@Composable
fun ProfileSetupScreen(navController: NavController) {

    Column(
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_medium),
            start = dimensionResource(R.dimen.padding_medium),
            end = dimensionResource(R.dimen.padding_medium)

        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text(text = "+375333733413 - Иван", fontSize = 18.sp)
            }
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text(text = "+375333733414 - Злата", fontSize = 18.sp)
            }
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text(text = "+375333733415 - Анна", fontSize = 18.sp)
            }
        }

    }
}


@Composable
@Preview
fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(rememberNavController())
}