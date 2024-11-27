package com.jddev.simplemusic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jddev.simplemusic.AppContainer
import com.jddev.simplemusic.ui.home.navigation.HomeNavGraph
import com.jddev.simplemusic.ui.settings.SettingsRoute
import com.jddev.simpletouch.ui.component.StDoubleBackPressToExit
import com.jddev.simpletouch.ui.component.transition.composableSlideTransition

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
    navigationActions: MainNavigationActions,
    startDestination: String = MainNavigation.Home.route,
) {
    StDoubleBackPressToExit()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composableSlideTransition(
            route = MainNavigation.Home.route,
        ) {
            HomeNavGraph (
                navigateToSettings = navigationActions.navigateToSettings,
            )
        }
        composableSlideTransition(
            route = MainNavigation.Settings.route,
        ) {
            SettingsRoute(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
