package com.example.minibus.screens


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(cities: List<String>) {

    Scaffold(topBar = { LocationSearchBar() }) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(cities) { item ->
                CityItem(city = item)
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
fun CityItem(city: String) {
    Button(onClick = { /*TODO*/ }) {
        Text(text = city, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
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

