package com.jddev.simplemusic.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.jddev.simplemusic.AppContainer
import com.jddev.simplemusic.ui.navigation.HomeNavGraph
import com.jddev.simplemusic.ui.navigation.SmBottomNavigation
import com.jddev.simpletouch.ui.theme.StUiTheme

@Composable
fun SimpleMusicApp(appContainer: AppContainer) {
    StUiTheme {
        val navController = rememberNavController()
        SmBottomNavigation(navController = navController) {
            HomeNavGraph(
                appContainer = appContainer,
                navController = navController,
            )
        }
    }
}