package com.example.minibus.navigation

import android.util.Log
import androidx.compose.runtime.State
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.minibus.screens.AddPassengersScreen
import com.example.minibus.screens.CheckoutScreen
import com.example.minibus.screens.LocationSearchScreen
import com.example.minibus.screens.ResultSearchScreen
import com.example.minibus.screens.RouteConfigurationScreen
import com.example.minibus.screens.StoppingPointsScreen
import com.example.minibus.snackbarClasses.SnackbarDelegate
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.UserViewModel
import java.time.format.DateTimeFormatter

fun NavGraphBuilder.routeConfigurationGraph(
    navController: NavController,
    viewModel: OrderViewModel,
    uiState: State<TicketUiState>,
    snackbarDelegate: SnackbarDelegate,
    userViewModel: UserViewModel,
) {
    navigation(
        startDestination = BottomNavigationScreen.RouteConfigurationScreen.route,
        route = "option"
    ) {
        composable(BottomNavigationScreen.RouteConfigurationScreen.route) {
            RouteConfigurationScreen(
                onStartAddPassengerClicked = { navController.navigate("addPassengers") },
                onDepartureSelectionClick = { navController.navigate("selectionDeparture") },
                onArrivalSelectionClick = { navController.navigate("selectionArrival") },
                onFindTripsClick = { navController.navigate("resultSearchScreen") },
                changeDirectionClick = { viewModel.changeDirections() },
                onShowCalendarClick = { viewModel.showCalendar() },
                selection = viewModel.calendarSelection,
                state = viewModel.calendarState,
                dateText = uiState.value.departureDate.format(DateTimeFormatter.ofPattern("d MMMM"))
                    .toString(),
                numberPassengers = uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats,
                departureCity = uiState.value.departureCity,
                arrivalCity = uiState.value.arrivalCity
            )
        }
        composable("addPassengers") {
            AddPassengersScreen(
                viewModel,
                uiState,
                navController
            )

        }
        composable("selectionArrival") {
            LocationSearchScreen(
                viewModel, navController, departure = false
            )
        }
        composable("selectionDeparture") {
            LocationSearchScreen(
                viewModel, navController, departure = true
            )
        }
        composable("resultSearchScreen") {

            ResultSearchScreen(uiState, viewModel, navController, snackbarDelegate)
        }

        composable("checkoutScreen/{tripId}") { navBackStackEntry ->

            val tripIdString = navBackStackEntry.arguments?.getString("tripId")

            val tripId = tripIdString?.toIntOrNull() ?: 0
            Log.d("CheckoutScreen", "Received trip ID as string: $tripIdString")

            Log.d("CheckoutScreen", "Parsed trip ID: $tripId")
            CheckoutScreen(uiState, userViewModel, navController,snackbarDelegate, tripId)
        }

        composable("selectionDeparturePoint") {
            StoppingPointsScreen(uiState, true, viewModel, navController)
        }

        composable("selectionArrivalPoint") {
            StoppingPointsScreen(uiState, false, viewModel, navController)
        }
    }
}
