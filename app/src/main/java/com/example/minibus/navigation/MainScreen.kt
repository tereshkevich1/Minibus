package com.example.minibus.navigation

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.screens.PersonalInformationScreen
import com.example.minibus.screens.ProfileSetupScreen
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.UserViewModel

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(userViewModel: UserViewModel) {
    val items = listOf(
        BottomNavigationScreen.RouteConfigurationScreen,
        BottomNavigationScreen.TravelHistoryScreen,
        BottomNavigationScreen.ContactsScreen,
        BottomNavigationScreen.ProfileSetupScreen
    )
    val navController = rememberNavController()

    val viewModel: OrderViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()


    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showTopBar = currentRoute != BottomNavigationScreen.RouteConfigurationScreen.route // Пример условия для показа TopBar
    val topBarTitle = getTitleForRoute(currentRoute)

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(text = topBarTitle) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(navController, items)
        }
    ) { innerPadding ->
        MainScreenNavigationConfigurations(
            navController, Modifier.padding(innerPadding),
            viewModel, uiState, userViewModel
        )
    }
}

fun getTitleForRoute(route: String?): String {
    return when (route) {
        BottomNavigationScreen.RouteConfigurationScreen.route -> "Настройка маршрута"
        BottomNavigationScreen.TravelHistoryScreen.route -> "История поездок"
        BottomNavigationScreen.ContactsScreen.route -> "Контакты"
        BottomNavigationScreen.ProfileSetupScreen.route -> "Настройка профиля"
        else -> "Приложение"
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
    uiState: State<TicketUiState>,
    userViewModel: UserViewModel

    ) {
    NavHost(
        navController,
        startDestination = "option",
        modifier = modifier
    ) {

        routeConfigurationGraph(navController, viewModel, uiState)
        historyGraph(navController, viewModel, uiState)
        composable(BottomNavigationScreen.ContactsScreen.route) { ProfileSetupScreen() }
        composable(BottomNavigationScreen.ProfileSetupScreen.route) { PersonalInformationScreen(userViewModel) }
    }
}




