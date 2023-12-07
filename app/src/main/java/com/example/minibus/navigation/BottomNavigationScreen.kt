package com.example.minibus.navigation

import androidx.annotation.StringRes
import com.example.minibus.R

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