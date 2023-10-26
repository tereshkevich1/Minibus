package com.example.minibus.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel

@Composable
fun AddPassengersScreen(viewModel: OrderViewModel, uiState: State<TicketUiState>, navController: NavController) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
            AddLine(
                text = stringResource(id = R.string.adult_passenger),
                numberSeats = uiState.value.numberAdultsSeats.toString(),
                increaseNumberSeats = { viewModel.increaseNumberAdultsSeats() },
                decreaseNumberSeats = { viewModel.decreaseNumberAdultsSeats() }
            )
            AddLine(
                text = stringResource(id = R.string.adult_passenger),
                numberSeats = uiState.value.numberChildrenSeats.toString(),
                increaseNumberSeats = { viewModel.increaseNumberChildrenSeats() },
                decreaseNumberSeats = { viewModel.decreaseNumberChildrenSeats() }
            )

            Spacer(modifier = Modifier.weight(1f))
            ElevatedButton(
                onClick = { navController.popBackStack() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = stringResource(R.string.continue_name))
            }

        }
    }
}

@Composable
fun AddLine(
    text: String,
    numberSeats: String,
    increaseNumberSeats: () -> Unit,
    decreaseNumberSeats: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = decreaseNumberSeats,
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_24),
                contentDescription = null
            )
        }
        Text(
            text = numberSeats,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small)
            )
        )
        IconButton(
            onClick = increaseNumberSeats,
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = null,
            )
        }
    }
}

@Composable
@Preview
fun AddPassengersScreenDarkPreview() {
    MinibusTheme(useDarkTheme = true) {

            val viewModel: OrderViewModel = viewModel()
            val uiState = viewModel.uiState.collectAsState()
            AddPassengersScreen(viewModel = viewModel, uiState = uiState,rememberNavController())


    }
}

@Composable
@Preview
fun AddPassengersScreenLightPreview() {
    MinibusTheme(useDarkTheme = false) {
            val viewModel: OrderViewModel = viewModel()
            val uiState = viewModel.uiState.collectAsState()
            AddPassengersScreen(viewModel = viewModel, uiState = uiState, rememberNavController())

    }
}


