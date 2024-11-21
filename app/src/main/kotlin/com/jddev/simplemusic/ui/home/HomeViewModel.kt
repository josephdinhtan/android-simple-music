package com.jddev.simplemusic.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import com.jddev.simplemusic.domain.usecase.RequestDeviceScanUseCase
import com.jddev.simplemusic.ui.components.TrackEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val requestDeviceScanUseCase: RequestDeviceScanUseCase,
    private val musicControllerRepository: MusicControllerRepository,
) : ViewModel() {

    private val _showFullTrackScreen = MutableStateFlow<Boolean>(false)
    val showFullTrackScreen = _showFullTrackScreen.asStateFlow()

    private val _tracksLoaded = MutableStateFlow<List<Track>>(listOf())
    val tracksLoaded = _tracksLoaded.asStateFlow()

    val currentTrack = musicControllerRepository.currentTrack
    val playerState = musicControllerRepository.playerState

    fun onShowTrackFullScreen() = _showFullTrackScreen.tryEmit(true)
    fun onDismissTrackFullScreen() = _showFullTrackScreen.tryEmit(false)

    fun playATrack(trackId: String) {
        musicControllerRepository.play(trackId)
    }

    fun scanDevice() {
        // load all tracks
        viewModelScope.launch {
            val allTracks = requestDeviceScanUseCase()
            Timber.d("All tracks: ${allTracks.size}")
            _tracksLoaded.emit(allTracks)
            musicControllerRepository.addMediaItems(tracks = allTracks)
        }
    }

    fun onTrackEvent(event: TrackEvent) {
        when(event) {
            TrackEvent.Pause -> musicControllerRepository.pauseCurrentTrack()
            TrackEvent.Resume -> musicControllerRepository.resumeCurrentTrack()
            TrackEvent.SkipToNext -> musicControllerRepository.skipToNextTrack()
            TrackEvent.SkipToPrevious -> musicControllerRepository.skipToPreviousTrack()
            else -> {
                Timber.e("Event not handled: $event")
            }
        }
    }
}