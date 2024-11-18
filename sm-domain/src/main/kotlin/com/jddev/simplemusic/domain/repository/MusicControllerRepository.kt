package com.jddev.simplemusic.domain.repository

import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MusicControllerRepository {

    val playerState: StateFlow<PlayerState>
    val currentMusic: StateFlow<Track?>
    val currentPosition: StateFlow<Long>
    val totalDuration: StateFlow<Long>
    val isShuffleEnabled: StateFlow<Boolean>
    val isRepeatOneEnabled: StateFlow<Boolean>

    fun addMediaItems(tracks: List<Track>)

    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun getCurrentPosition(): Long?

    fun destroy()

    fun skipToNextTrack()

    fun skipToPreviousTrack()

    fun getCurrentTrack(): Track?

    fun seekTo(position: Long)
}