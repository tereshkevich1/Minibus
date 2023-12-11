package com.example.minibus.navigation

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.minibus.models.UserTravelHistory
import com.example.minibus.network.JsonFormat
import com.example.minibus.screens.TicketDetailsScreen
import com.example.minibus.screens.TripHistoryScreen

fun NavGraphBuilder.historyGraph(
    navController: NavController,
    userId: Int,
) {

    navigation(
        startDestination = BottomNavigationScreen.TravelHistoryScreen.route,
        route = "history"
    ) {

        composable(BottomNavigationScreen.TravelHistoryScreen.route) {

            TripHistoryScreen(navController,userId)
        }
        composable("detailsScreen/{userTravelHistoryJson}") { backStackEntry ->


            val encodedJson = backStackEntry.arguments?.getString("userTravelHistoryJson")

            // Раскодирование JSON строки
            val decodedJson = encodedJson?.let { Uri.decode(it) }

            // Десериализация JSON строки обратно в объект UserTravelHistory
            val userTravelHistory = decodedJson?.let {
                JsonFormat.instance.decodeFromString<UserTravelHistory>(it)
            }
            Log.d("detailsScreenCOMPOSABLE", "wo $userTravelHistory")
            TicketDetailsScreen(userTravelHistory, navController)
        }
    }
}