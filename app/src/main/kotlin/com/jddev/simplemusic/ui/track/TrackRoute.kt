package com.jddev.simplemusic.ui.track

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.MusicControllerUiState
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.ui.components.TrackEvent

@Composable
fun TrackRoute(
    trackViewModel: TrackViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val playerState = trackViewModel.playerState.collectAsState()
    val currentPosition = trackViewModel.currentPosition.collectAsState()
    val totalDuration = trackViewModel.totalDuration.collectAsState()

    val musicControllerUiState = MusicControllerUiState(
        playerState = playerState.value,
        currentTrack = Track("Test Track", "Test Track", "Test single", "", ""),
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
        onBack = onBack,
    )
}