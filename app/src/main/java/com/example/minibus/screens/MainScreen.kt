package com.example.minibus.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.primarySurface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object RouteConfigurationScreen :
        BottomNavigationScreen("Search", R.string.configuration_route, Icons.Filled.Search)

    object RouteConfigurationScree :
        BottomNavigationScreen("Sear", R.string.configuration_route, Icons.Filled.List)

    object RouteConfigurationScre :
        BottomNavigationScreen("Sea", R.string.configuration_route, Icons.Filled.Call)

    object RouteConfigurationScr :
        BottomNavigationScreen("Se", R.string.configuration_route, Icons.Filled.Person)
}


@Composable
fun MainScreen() {
    val items = listOf(
        BottomNavigationScreen.RouteConfigurationScreen,
        BottomNavigationScreen.RouteConfigurationScree,
        BottomNavigationScreen.RouteConfigurationScre,
        BottomNavigationScreen.RouteConfigurationScr
    )
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
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
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavigationScreen.RouteConfigurationScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationScreen.RouteConfigurationScreen.route) { RouteConfigurationScreen() }
            composable(BottomNavigationScreen.RouteConfigurationScree.route) { RouteConfigurationScreen() }
            composable(BottomNavigationScreen.RouteConfigurationScre.route) { ProfileSetupScreen() }
            composable(BottomNavigationScreen.RouteConfigurationScr.route) { ProfileSetupScreen() }
        }
    }
}

