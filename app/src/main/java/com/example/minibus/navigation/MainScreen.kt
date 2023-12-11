package com.example.minibus.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.screens.LoadingScreen
import com.example.minibus.screens.LoginScreen
import com.example.minibus.screens.PasswordChangeScreen
import com.example.minibus.screens.PersonalInformationScreen
import com.example.minibus.screens.ProfileSetupScreen
import com.example.minibus.screens.RegistrationScreen
import com.example.minibus.snackbarClasses.SnackbarDelegate
import com.example.minibus.state_models.TicketUiState
import com.example.minibus.vm.OrderViewModel
import com.example.minibus.vm.UserViewModel
import kotlinx.coroutines.delay


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

    var startDestination by remember { mutableStateOf("loadingScreen") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        isLoading = false
        startDestination = if (userViewModel.userUiState.value.userId > 0) {
            "option"
        } else {
            "logInScreen"
        }

        navController.navigate(startDestination) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val noTopBarRoutes = remember {
        setOf(
            BottomNavigationScreen.RouteConfigurationScreen.route,
            "logInScreen",
            "loadingScreen"
        )
    }
    val noBottomBarRoutes = remember {
        setOf(
            "signUpScreen",
            "logInScreen",
            "addPassengers",
            "loadingScreen"
        )
    }

    fun showTopBar() = !isLoading && currentRoute !in noTopBarRoutes
    fun showBottomBar() = !isLoading && currentRoute !in noBottomBarRoutes

    val topBarTitle = getTitleForRoute(currentRoute, uiState)
    val showTopBarBackIcon = items.any { it.route == currentRoute }


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val snackbarDelegate = remember {
        SnackbarDelegate(snackbarHostState, coroutineScope)
    }

    Log.d("startDestination", startDestination)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                val backgroundColor = snackbarDelegate.snackbarBackgroundColor
                val bottomPadding = snackbarDelegate.snackbarBottomPadding
                val startEndPadding = dimensionResource(id = R.dimen.padding_medium)

                Snackbar(
                    containerColor = backgroundColor, modifier = Modifier
                        .padding(
                            bottom = bottomPadding,
                            start = startEndPadding,
                            end = startEndPadding
                        )
                ) {
                    Text(
                        text = it.visuals.message,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }


            }
        },
        topBar = {
            if (showTopBar()) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        if (!showTopBarBackIcon) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                                    contentDescription = "Назад"
                                )
                            }
                        }
                    },
                    modifier = Modifier.shadow(6.dp)
                )
            }
        },
        bottomBar = {
            if (showBottomBar()) {
                BottomNavigationBar(navController, items)
            }
        }
    ) { innerPadding ->
        NavigationConfigurations(
            navController, Modifier.padding(innerPadding),
            viewModel, uiState, userViewModel, snackbarDelegate,
            startDestination
        )
    }
}

fun getTitleForRoute(route: String?, uiState: State<TicketUiState>): String {
    return when (route) {
        BottomNavigationScreen.RouteConfigurationScreen.route -> "Настройка маршрута"
        BottomNavigationScreen.TravelHistoryScreen.route -> "История поездок"
        BottomNavigationScreen.ContactsScreen.route -> "Контакты"
        BottomNavigationScreen.ProfileSetupScreen.route -> "Настройка профиля"
        "resultSearchScreen" -> "${uiState.value.departureCity} - ${uiState.value.arrivalCity}"
        "checkoutScreen/{tripId}" -> "Оформление заказа"
        "selectionDeparturePoint" -> "${uiState.value.departureCity} - место посадки"
        "selectionArrivalPoint" -> "${uiState.value.arrivalCity} - место высадки"
        "selectionArrival" -> "Город прибытия"
        "selectionDeparture" -> "Город отправления"
        "signUpScreen" -> "Регистрация"
        else -> "Minibus"
    }
}


@Composable
private fun NavigationConfigurations(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: OrderViewModel,
    uiState: State<TicketUiState>,
    userViewModel: UserViewModel,
    snackbarDelegate: SnackbarDelegate,
    startDestination: String

) {
    NavHost(
        navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        routeConfigurationGraph(navController, viewModel, uiState, snackbarDelegate, userViewModel)
        historyGraph(navController, userViewModel.userUiState.value.userId)
        composable(BottomNavigationScreen.ContactsScreen.route) {

            ProfileSetupScreen(navController)
        }
        composable(BottomNavigationScreen.ProfileSetupScreen.route) {
            //userViewModel.getUserData()
            Log.d("profile", "navigate to profile")
            PersonalInformationScreen(
                userViewModel,
                navController
            )
        }
        composable("logInScreen") {
            LoginScreen(userViewModel, navController, snackbarDelegate)
        }
        composable("signUpScreen") {
            RegistrationScreen(userViewModel, navController)
        }
        composable("loadingScreen") {
            LoadingScreen()
        }
        composable("changePasswordScreen") {
            PasswordChangeScreen(
                userViewModel.userUiState.collectAsState().value.userId,
                navController,
                snackbarDelegate
            )
        }
    }
}






