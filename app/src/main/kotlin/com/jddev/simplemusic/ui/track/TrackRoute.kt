package com.jddev.simplemusic.ui.track

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.ui.MusicControllerUiState
import com.jddev.simplemusic.ui.components.HomeMenu
import com.jddev.simplemusic.ui.components.SmBottomSheet
import com.jddev.simplemusic.ui.components.TrackEvent
import com.jddev.simplemusic.ui.components.TrackInfoContent


@Composable
fun TrackRoute(
    trackViewModel: TrackViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    onBack: () -> Unit,
) {
    val playerState = trackViewModel.playerState.collectAsState()
    val currentPosition = trackViewModel.currentPosition.collectAsState()
    val totalDuration = trackViewModel.totalDuration.collectAsState()
    val currentTrack = trackViewModel.currentTrack.collectAsState()
    var showBottomSheetMenu by remember { mutableStateOf(false) }
    var showBottomSheetTrackInfo by remember { mutableStateOf(false) }

    if(currentTrack.value == null) return

    val musicControllerUiState = MusicControllerUiState(
        playerState = playerState.value,
        currentTrack = currentTrack.value!!,
        currentPosition = currentPosition.value,
        totalDuration = totalDuration.value,
    )
    TrackScreen(
        onEvent = {
            when (it) {
                TrackEvent.Play -> trackViewModel.play()
                TrackEvent.Pause -> trackViewModel.pause()
                TrackEvent.Resume -> trackViewModel.resume()
                TrackEvent.SkipToNext -> trackViewModel.skipToNext()
                TrackEvent.SkipToPrevious -> trackViewModel.skipToPrevious()
                TrackEvent.Fetch -> TODO()
                is TrackEvent.OnSongSelected -> TODO()
                is TrackEvent.SeekTrackToPosition -> trackViewModel.seekTrackToPosition(it.position)
            }
        },
        musicControllerUiState = musicControllerUiState,
        onShowMenu = { showBottomSheetMenu = true },
        onBack = onBack,
    )

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
                },
                onShowBottomSheetTrackInfo = {
                    if(currentTrack.value != null) {
                        showBottomSheetMenu = false
                        showBottomSheetTrackInfo = true
                    }
                }
            )
        }
    }

    if (showBottomSheetTrackInfo) {
        SmBottomSheet(
            onDismissRequest = { showBottomSheetTrackInfo = false },
        ) {
            TrackInfoContent(
                modifier = Modifier.fillMaxWidth(),
                track = currentTrack.value!!,
            )
        }
    }
}