package com.jddev.simplemusic.ui

import androidx.navigation.NavHostController

sealed class MainNavigation(val route: String) {

    data object Home : MainNavigation("home")
    data object Settings : MainNavigation("settings")
    data object Track : MainNavigation("track/{itemId}") {
        fun createRoute(itemId: String) = "track/$itemId"
    }
}

class MainNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(MainNavigation.Home.route) {
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(MainNavigation.Settings.route) {
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToTrack: (trackId: String) -> Unit =  { trackId ->
        navController.navigate(MainNavigation.Track.createRoute(trackId)) {
            launchSingleTop = true
            restoreState = true
        }
    }
}