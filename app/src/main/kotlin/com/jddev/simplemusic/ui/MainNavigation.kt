package com.jddev.simplemusic.ui

import androidx.navigation.NavHostController

sealed class MainNavigation(val route: String) {
    data object Home : MainNavigation("home")
    data object Settings : MainNavigation("settings")
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
}