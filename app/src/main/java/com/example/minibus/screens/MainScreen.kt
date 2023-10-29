package com.example.minibus.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.vm.OrderViewModel

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object RouteConfigurationScreen :
        BottomNavigationScreen("Search", R.string.search, Icons.Filled.Search)

    object TravelHistoryScreen :
        BottomNavigationScreen("Story", R.string.story, Icons.Filled.List)

    object ContactsScreen :
        BottomNavigationScreen("Contacts", R.string.contacts, Icons.Filled.Call)

    object ProfileSetupScreen :
        BottomNavigationScreen("Profile", R.string.profile, Icons.Filled.Person)
}


@Composable
fun MainScreen() {
    val items = listOf(
        BottomNavigationScreen.RouteConfigurationScreen,
        BottomNavigationScreen.TravelHistoryScreen,
        BottomNavigationScreen.ContactsScreen,
        BottomNavigationScreen.ProfileSetupScreen
    )
    val navController = rememberNavController()

    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, items)
        }
    ) { innerPadding ->
        MainScreenNavigationConfigurations(
            navController, Modifier.padding(innerPadding),
            viewModel, uiState
        )
    }
}


@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavigationScreen>
) {
    BottomNavigation(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface) {
        //
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        //достаем текущий экран
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun MainScreenNavigationConfigurations(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: OrderViewModel,
    uiState: State<TicketUiState>
) {
    NavHost(
        navController,
        startDestination = "option",
        modifier = modifier
    ) {

        routeConfigurationGraph(navController, viewModel, uiState)
        composable(BottomNavigationScreen.TravelHistoryScreen.route) { ProfileSetupScreen() }
        composable(BottomNavigationScreen.ContactsScreen.route) { ProfileSetupScreen() }
        composable(BottomNavigationScreen.ProfileSetupScreen.route) { ProfileSetupScreen() }
    }
}


private fun NavGraphBuilder.routeConfigurationGraph(
    navController: NavController,
    viewModel: OrderViewModel,
    uiState: State<TicketUiState>
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
                onShowCalendarClick = { viewModel.showCalendar() },
                selection = viewModel.calendarSelection,
                state = viewModel.calendarState,
                dateText = uiState.value.departureDate.toString(),
                numberPassengersText = (uiState.value.numberChildrenSeats + uiState.value.numberAdultsSeats).toString(),
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
            ResultSearchScreen(uiState,{},{},{})
        }
    }
}


