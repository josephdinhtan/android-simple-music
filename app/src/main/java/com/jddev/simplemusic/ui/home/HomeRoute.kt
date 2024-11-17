package com.jddev.simplemusic.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    onShowTrackScreen: (trackId: String) -> Unit,
) {
    HomeScreen(
        navigateToSettings = navigateToSettings,
        onShowTrackScreen = onShowTrackScreen,
    )
}