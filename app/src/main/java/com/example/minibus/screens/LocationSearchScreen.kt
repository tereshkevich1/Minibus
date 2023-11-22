package com.example.minibus.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.models.City
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.LocationViewModel
import com.example.minibus.vm.MinibusUiState
import com.example.minibus.vm.OrderViewModel


@Composable
fun LocationSearchScreen(
    viewModel: OrderViewModel, navController: NavController,
    departure: Boolean
) {
    val locationViewModel: LocationViewModel = viewModel()

    val locationUIState by rememberUpdatedState(locationViewModel.locationUIState)
    Surface(modifier = Modifier.fillMaxSize()) {
        when (locationUIState) {

            is MinibusUiState.Success -> LocationsPanel(
                (locationUIState as MinibusUiState.Success<MutableList<City>>).data,
                viewModel,
                navController,
                departure
            )

            is MinibusUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(44.dp))
                }
            }

            is MinibusUiState.Error ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    ErrorScreen(

                        modifier = Modifier.fillMaxSize(),
                        (locationUIState as MinibusUiState.Error).exception,
                        tryAgainClick = { locationViewModel.loadData() }
                    )
                }
        }
    }
}

@Composable
fun LocationsPanel(
    cities: List<City>, viewModel: OrderViewModel, navController: NavController,
    departure: Boolean
) {
    LazyColumn() {
        items(cities) { item ->
            LocationItem(location = item.name, setLocationClick = {
                if (departure) {
                    viewModel.setDepartureCity(item)
                    navController.popBackStack()
                } else {
                    viewModel.setArrivalCity(item)
                    navController.popBackStack()
                }
            })
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun LocationSearchBar() {
    TopAppBar(
        title = {
            TextField(
                value = "", onValueChange = {},
                singleLine = true,
                label = { Text(stringResource(R.string.search_state)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = null
                )
            }
        },

        )
}

@Composable
fun LocationItem(location: String, setLocationClick: () -> Unit) {
    Button(
        onClick = setLocationClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = location, modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_small), top = dimensionResource(
                        id = R.dimen.padding_small
                    ), bottom = dimensionResource(id = R.dimen.padding_small)
                )
            )
        }
    }
}

@Composable
@Preview
fun StateItemPreview() {
    //StateItem()
}

@Composable
@Preview
fun LocationSearchScreenDarkPreview() {
    MinibusTheme(useDarkTheme = true) {
        val viewModel: OrderViewModel = viewModel()
        LocationSearchScreen(viewModel, rememberNavController(), true)
    }
}

@Composable
@Preview
fun LocationSearchScreenLightPreview() {
    val viewModel: OrderViewModel = viewModel()
    MinibusTheme(useDarkTheme = false) {
        LocationSearchScreen(viewModel, rememberNavController(), false)
    }
}

