package com.jddev.simplemusic.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsRoute(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    bottomPadding: Dp,
    onBack: () -> Unit,
) {
    SettingsScreen(
        bottomPadding = bottomPadding,
        onThemeChange = { /* call to settingsViewModel.changeTheme() */ },
        onBack = onBack,
    )
}