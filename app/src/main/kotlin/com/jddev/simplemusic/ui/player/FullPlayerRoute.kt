package com.jddev.simplemusic.ui.player

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
fun FullPlayerRoute(
    fullPlayerViewModel: FullPlayerViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    onBack: () -> Unit,
) {
    val playerState = fullPlayerViewModel.playerState.collectAsState()
    val currentPosition = fullPlayerViewModel.currentPosition.collectAsState()
    val totalDuration = fullPlayerViewModel.totalDuration.collectAsState()
    val currentTrack = fullPlayerViewModel.currentTrack.collectAsState()
    var showBottomSheetMenu by remember { mutableStateOf(false) }
    var showBottomSheetTrackInfo by remember { mutableStateOf(false) }

    if(currentTrack.value == null) return

    val musicControllerUiState = MusicControllerUiState(
        playerState = playerState.value,
        currentTrack = currentTrack.value!!,
        currentPosition = currentPosition.value,
        totalDuration = totalDuration.value,
    )
    FullPlayerScreen(
        getAlbumArt = fullPlayerViewModel::getAlbumArt,
        onEvent = {
            when (it) {
                TrackEvent.Play -> fullPlayerViewModel.play()
                TrackEvent.Pause -> fullPlayerViewModel.pause()
                TrackEvent.Resume -> fullPlayerViewModel.resume()
                TrackEvent.SkipToNext -> fullPlayerViewModel.skipToNext()
                TrackEvent.SkipToPrevious -> fullPlayerViewModel.skipToPrevious()
                TrackEvent.Fetch -> TODO()
                is TrackEvent.OnSongSelected -> TODO()
                is TrackEvent.SeekTrackToPosition -> fullPlayerViewModel.seekTrackToPosition(it.position)
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
                getAlbumArt = fullPlayerViewModel::getAlbumArt,
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
                getAlbumArt = fullPlayerViewModel::getAlbumArt,
            )
        }
    }
}