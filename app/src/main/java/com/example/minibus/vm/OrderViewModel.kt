package com.example.minibus.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.minibus.models.City
import com.example.minibus.models.StopPoint
import com.example.minibus.state_models.TicketUiState
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class OrderViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState = _uiState.asStateFlow()

    //CalendarDialog
    val calendarSelection: CalendarSelection = CalendarSelection.Date(onSelectDate = {
        //when selecting a date in the calendar, the departureDate changes
        _uiState.update { currentState ->
            currentState.copy(departureDate = it)
        }
    })

    val calendarState = UseCaseState(false)
    fun showCalendar() {
        calendarState.show()
    }
    //_

    fun changeDirections() {
        _uiState.update { currentState ->
            currentState.copy(
                departureCity = currentState.arrivalCity,
                departureCityId = currentState.arrivalCityId,
                arrivalCityId = currentState.departureCityId,
                arrivalCity = currentState.departureCity
            )
        }
    }


    // 4fun for AddPassengersScreen
    fun increaseNumberAdultsSeats() {
        if (uiState.value.numberAdultsSeats + uiState.value.numberChildrenSeats < 3) {
            _uiState.update { currentState ->
                currentState.copy(
                    numberAdultsSeats = currentState.numberAdultsSeats.plus(
                        1
                    )
                )
            }
        }
    }

    fun decreaseNumberAdultsSeats() {
        val updatedNumber = uiState.value.numberAdultsSeats.minus(1)
        if (updatedNumber >= 1) {
            _uiState.update { currentState ->
                currentState.copy(
                    numberAdultsSeats = currentState.numberAdultsSeats.minus(
                        1
                    )
                )
            }
        }
    }

    fun increaseNumberChildrenSeats() {
        if (uiState.value.numberAdultsSeats + uiState.value.numberChildrenSeats < 3) {
            _uiState.update { currentState ->
                currentState.copy(
                    numberChildrenSeats = currentState.numberChildrenSeats.plus(
                        1
                    )
                )
            }
        }
    }

    fun decreaseNumberChildrenSeats() {
        val updatedNumber = uiState.value.numberChildrenSeats.minus(1)
        if (updatedNumber >= 0) {
            _uiState.update { currentState -> currentState.copy(numberChildrenSeats = updatedNumber) }
        }
    }
    //_

    fun setDepartureCity(departureCity: City) {
        Log.d("setDepartureCity","${departureCity.id} & ${_uiState.value.arrivalCityId}")
        if (departureCity.id == _uiState.value.arrivalCityId) {
            _uiState.update { currentState ->
                currentState.copy(
                    departureCity = departureCity.name,
                    departureCityId = departureCity.id,
                    arrivalCity = currentState.departureCity,
                    arrivalCityId = currentState.departureCityId
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    departureCity = departureCity.name,
                    departureCityId = departureCity.id
                )
            }
        }

    }

    fun setArrivalCity(arrivalCity: City) {
        if (arrivalCity.id == _uiState.value.departureCityId) {
            _uiState.update { currentState ->
                currentState.copy(
                    arrivalCity = arrivalCity.name,
                    arrivalCityId = arrivalCity.id,
                    departureCity = currentState.arrivalCity,
                    departureCityId = currentState.arrivalCityId
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    arrivalCity = arrivalCity.name,
                    arrivalCityId = arrivalCity.id
                )
            }
        }
    }

    private fun setDeparturePoint(departurePoint: StopPoint) {
        _uiState.update { currentState ->
            currentState.copy(
                departurePointId = departurePoint.id,
                departurePoint = departurePoint.name
            )
        }
    }

    private fun setArrivalPoint(arrivalPoint: StopPoint) {
        _uiState.update { currentState ->
            currentState.copy(
                arrivalPointId = arrivalPoint.id,
                arrivalPoint = arrivalPoint.name
            )
        }
    }

    fun changeStoppingPoint(departurePoint: Boolean, point: StopPoint) {
        if (departurePoint) {
            setDeparturePoint(point)
        } else {
            setArrivalPoint(point)
        }
    }

}