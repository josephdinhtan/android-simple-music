package com.jddev.simplemusic.ui.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor (
    private val musicControllerRepository: MusicControllerRepository,
): ViewModel() {
    fun pause() {
        musicControllerRepository.pause()
    }

    fun skipToNext() {
        musicControllerRepository.skipToNextTrack()
    }

    fun resume() {
        musicControllerRepository.resume()
    }

    fun skipToPrevious() {
        musicControllerRepository.skipToPreviousTrack()
    }

    fun play() {
        TODO("play() not implemented")
        //musicControllerRepository.play()
    }

    fun seekTrackToPosition(position: Long) {
        musicControllerRepository.seekTo(position)
    }

    val playerState = musicControllerRepository.playerState
    val currentPosition = musicControllerRepository.currentPosition
    val totalDuration = musicControllerRepository.totalDuration

    init {
        viewModelScope.launch {
            delay(3000)

            musicControllerRepository.addMediaItems(listOf())
            musicControllerRepository.play(0)
        }
    }
}