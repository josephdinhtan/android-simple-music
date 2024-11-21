package com.jddev.simplemusic.ui

import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track

data class MusicControllerUiState(
    val playerState: PlayerState? = null,
    val currentTrack: Track,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)