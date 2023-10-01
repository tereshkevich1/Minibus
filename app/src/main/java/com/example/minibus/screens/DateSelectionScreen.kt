package com.example.minibus.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.OrderViewModel
import com.maxkeppeler.sheets.calendar.CalendarDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector() {

    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()

    CalendarDialog(state = viewModel.calendarState, selection = viewModel.calendarSelection)

    Box(modifier = Modifier.fillMaxSize()) {
        ElevatedButton(
            onClick = { viewModel.showCalendar() }
        ) {
            Text(text = "Show PopUp")
        }

        Text(text = uiState.value.departureDate.toString())
    }

}


@Composable
@Preview
fun DataSelectorPreview() {
    MinibusTheme(useDarkTheme = true) {
        Surface {
            DateSelector()
        }
    }
}