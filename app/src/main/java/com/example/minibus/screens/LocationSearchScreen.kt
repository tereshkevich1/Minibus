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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(cities: List<String>) {
    val viewModel: OrderViewModel = viewModel()
    //val uiState = viewModel.uiState.collectAsState()
    Scaffold(topBar = { LocationSearchBar() }) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(cities) { item ->
                CityItem(city = item) { viewModel.setDepartureCity(item) }
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
                modifier = Modifier.fillMaxWidth()
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
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
fun CityItem(city: String, setCityClick: () -> Unit) {
    Button(
        onClick = setCityClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = city, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
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
        LocationSearchScreen(cities)
    }
}

@Composable
@Preview
fun LocationSearchScreenLightPreview() {
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
        LocationSearchScreen(cities)
    }
}

