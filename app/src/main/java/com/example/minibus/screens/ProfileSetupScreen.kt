package com.example.minibus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
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
        Button(onClick = { /*TODO*/ }) {
            Text(text = navController.currentBackStackEntry?.savedStateHandle?.get<String>("hello_key")?:"q")
        }
    }
}


@Composable
@Preview
fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(rememberNavController())
}