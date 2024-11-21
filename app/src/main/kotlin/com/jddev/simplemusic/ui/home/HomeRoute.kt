package com.jddev.simplemusic.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.ui.components.FullScreenBottomSheet
import com.jddev.simplemusic.ui.components.HomeMenu
import com.jddev.simplemusic.ui.components.SmBottomSheet
import com.jddev.simplemusic.ui.track.TrackRoute

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    onShowTrackFullScreen: () -> Unit,
) {
    val allTracks = homeViewModel.tracksLoaded.collectAsState()
    val currentTrack = homeViewModel.currentTrack.collectAsState()
    val playerState = homeViewModel.playerState.collectAsState()
    val showTrackFullScreen = homeViewModel.showFullTrackScreen.collectAsState()
    var showBottomSheetMenu by remember { mutableStateOf(false) }

    Box {
        HomeScreen(
            allTracks = allTracks.value,
            currentTrack = currentTrack.value,
            playerState = playerState.value,
            navigateToSettings = {
                showBottomSheetMenu = true
            },
            requestScanDevice = { homeViewModel.scanDevice() },
            onShowTrackFullScreen = {
                homeViewModel.onShowTrackFullScreen()
            },
            onTrackSelected = { track ->
                homeViewModel.playATrack(track.id)
            },
            onTrackEvent = { event ->
                homeViewModel.onTrackEvent(event)
            }
        )
    }
    if (showTrackFullScreen.value) {
        FullScreenBottomSheet(
            onDismissRequest = { homeViewModel.onDismissTrackFullScreen() },
            content = {
                TrackRoute(
                    onBack = { homeViewModel.onDismissTrackFullScreen() },
                    navigateToSettings = navigateToSettings,
                )
            }
        )
    }
    if (showBottomSheetMenu) {
        SmBottomSheet(
            onDismissRequest = { showBottomSheetMenu = false },
        ) {
            HomeMenu(
                modifier = Modifier.fillMaxWidth(),
                track = currentTrack.value,
                navigateToSettings = {
                    showBottomSheetMenu = false
                    navigateToSettings()
                }
            )
        }
    }
}