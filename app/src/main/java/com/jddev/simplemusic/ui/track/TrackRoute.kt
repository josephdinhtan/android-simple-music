package com.jddev.simplemusic.ui.track

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.MusicControllerUiState
import com.jddev.simplemusic.ui.components.PlayerState

@Composable
fun TrackRoute(
    trackViewModel: TrackViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {

    val musicControllerUiState = MusicControllerUiState(
        playerState = PlayerState.PLAYING,
        currentTrack = Track("Test Track", "Test Track", "Test single", "", ""),
        currentPosition = 10,
        totalDuration = 100,
    )
    TrackScreen(
        onEvent = {},
        musicControllerUiState = musicControllerUiState,
        onBack = onBack,
    )
}