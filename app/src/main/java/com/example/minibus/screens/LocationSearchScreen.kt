package com.example.minibus.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    cities: List<String>, viewModel: OrderViewModel, navController: NavController,
    departure: Boolean
) {
    Scaffold(topBar = { LocationSearchBar() }) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(cities) { item ->
                CityItem(city = item, setCityClick = {
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
fun CityItem(city: String, setCityClick: () -> Unit) {
    Button(
        onClick = setCityClick,
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
                text = city, modifier = Modifier.padding(
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
        val cities = listOf(
            "Минск",
            "Москва",
            "Лондон",
            "Париж",
            "Берлин",
            "Рим",
            "Мадрид",
            "Амстердам",
            "Вена",
            "Прага",
            "Будапешт",
            "Варшава",
            "Стокгольм",
            "Осло",
            "Копенгаген",
            "Хельсинки",
            "Дублин",
            "Лиссабон",
            "Афины"
        )
        LocationSearchScreen(cities, viewModel, rememberNavController(), true)
    }
}

@Composable
@Preview
fun LocationSearchScreenLightPreview() {
    val viewModel: OrderViewModel = viewModel()
    MinibusTheme(useDarkTheme = false) {
        val cities = listOf(
            "Минск",
            "Москва",
            "Лондон",
            "Париж",
            "Берлин",
            "Рим",
            "Мадрид",
            "Амстердам",
            "Вена",
            "Прага",
            "Будапешт",
            "Варшава",
            "Стокгольм",
            "Осло",
            "Копенгаген",
            "Хельсинки",
            "Дублин",
            "Лиссабон",
            "Афины"
        )
        LocationSearchScreen(cities, viewModel, rememberNavController(), false)
    }
}

