package com.jddev.simplemusic.ui.track

import androidx.lifecycle.ViewModel
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor (
    private val musicControllerRepository: MusicControllerRepository,
): ViewModel() {
    val currentTrack = musicControllerRepository.currentTrack
    val playerState = musicControllerRepository.playerState
    val currentPosition = musicControllerRepository.currentPosition
    val totalDuration = musicControllerRepository.totalDuration

    fun pause() {
        musicControllerRepository.pauseCurrentTrack()
    }

    fun skipToNext() {
        musicControllerRepository.skipToNextTrack()
    }

    fun resume() {
        musicControllerRepository.resumeCurrentTrack()
    }

    fun skipToPrevious() {
        musicControllerRepository.skipToPreviousTrack()
    }

    fun play() {
        TODO("play() not implemented")
        //musicControllerRepository.play()
    }

    fun seekTrackToPosition(position: Long) {
        musicControllerRepository.seekCurrentTrackTo(position)
    }
}