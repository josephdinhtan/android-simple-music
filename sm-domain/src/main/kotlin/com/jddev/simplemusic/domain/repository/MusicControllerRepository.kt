package com.jddev.simplemusic.domain.repository

import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MusicControllerRepository {

    val currentTrack: StateFlow<Track?>
    val isReady: StateFlow<Boolean>

    val playerState: StateFlow<PlayerState>
    val currentPosition: StateFlow<Long>
    val totalDuration: StateFlow<Long>
    val isShuffleEnabled: StateFlow<Boolean>
    val isRepeatOneEnabled: StateFlow<Boolean>

    fun initializer()

    fun addMediaItems(tracks: List<Track>)

    fun play(mediaId: Long)

    fun resumeCurrentTrack()

    fun pauseCurrentTrack()

    fun skipToNextTrack()

    fun skipToPreviousTrack()

    fun seekCurrentTrackTo(position: Long)

    fun destroy()
}