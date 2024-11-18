package com.jddev.simplemusic.ui

import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.model.PlayerState

data class MusicControllerUiState(
    val playerState: PlayerState? = null,
    val currentTrack: Track? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)