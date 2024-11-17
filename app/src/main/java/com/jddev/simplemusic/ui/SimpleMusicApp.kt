package com.jddev.simplemusic.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.jddev.simplemusic.AppContainer
import com.jddev.simpletouch.ui.theme.StUiTheme

@Composable
fun SimpleMusicApp(appContainer: AppContainer) {
    StUiTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) { MainNavigationActions(navController) }
        MainNavGraph(
            appContainer = appContainer,
            navController = navController,
            navigationActions = navigationActions,
        )
    }
}