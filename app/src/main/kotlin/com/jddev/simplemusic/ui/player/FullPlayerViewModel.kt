package com.jddev.simplemusic.ui.player

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.jddev.simplemusic.domain.repository.PlaybackRepository
import com.jddev.simplemusic.domain.usecase.GetAlbumArtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FullPlayerViewModel @Inject constructor(
    private val getAlbumArtUseCase: GetAlbumArtUseCase,
    private val musicControllerRepository: PlaybackRepository,
) : ViewModel() {
    val currentTrack = musicControllerRepository.currentTrack
    val playerState = musicControllerRepository.playerState
    val currentPosition = musicControllerRepository.currentPosition
    val totalDuration = musicControllerRepository.totalDuration

    fun getAlbumArt(albumId: Long?, artistId: Long): Bitmap? {
        return getAlbumArtUseCase.invoke(albumId, artistId)
    }

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