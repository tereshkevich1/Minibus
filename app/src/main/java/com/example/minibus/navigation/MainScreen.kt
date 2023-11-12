package com.example.minibus.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val icon: Int
) {
    object RouteConfigurationScreen :
        BottomNavigationScreen("Search", R.string.search, R.drawable.baseline_search_24)

    object TravelHistoryScreen :
        BottomNavigationScreen("Story", R.string.story, R.drawable.baseline_list_24)

    object ContactsScreen :
        BottomNavigationScreen("Contacts", R.string.contacts, R.drawable.baseline_phone_24)

    object ProfileSetupScreen :
        BottomNavigationScreen("Profile", R.string.profile, R.drawable.baseline_person_24)
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
    val showTopBar =
        currentRoute != BottomNavigationScreen.RouteConfigurationScreen.route // Пример условия для показа TopBar
    val topBarTitle = getTitleForRoute(currentRoute)

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(text = topBarTitle, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_arrow_back_24),
                                contentDescription = "Назад"
                            )
                        }
                    },
                    modifier = Modifier.shadow(6.dp)
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
    NavigationBar(
        modifier = Modifier
            .height(60.dp)
    ) {
        //
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        //достаем текущий экран
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                modifier = Modifier.offset(y = -8.dp),
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = null,
                        modifier = Modifier.offset()
                    )
                },
                label = {
                    Text(
                        stringResource(screen.resourceId),
                        fontSize = 12.sp,
                        modifier = Modifier.offset(y = 16.dp)
                    )
                },
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
        composable(BottomNavigationScreen.ProfileSetupScreen.route) {
            PersonalInformationScreen(
                userViewModel
            )
        }
    }
}




