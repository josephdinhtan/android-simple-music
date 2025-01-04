package com.jddev.simplemusic.ui.library

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.PlaybackRepository
import com.jddev.simplemusic.domain.usecase.GetAlbumArtUseCase
import com.jddev.simplemusic.domain.usecase.GetAlbumsUseCase
import com.jddev.simplemusic.domain.usecase.GetAllTrackUseCase
import com.jddev.simplemusic.domain.usecase.GetArtistsUseCase
import com.jddev.simplemusic.ui.components.TrackEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAllTrackUseCase: GetAllTrackUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getArtistsUseCase: GetArtistsUseCase,
    private val musicControllerRepository: PlaybackRepository,
    private val getAlbumArtUseCase: GetAlbumArtUseCase,
) : ViewModel() {

    private val _showFullTrackScreen = MutableStateFlow<Boolean>(false)
    val showFullTrackScreen = _showFullTrackScreen.asStateFlow()

    private val _allTracks = MutableStateFlow<List<Track>>(listOf())
    val allTracks = getAllTrackUseCase.invoke()
    val artists = getArtistsUseCase.invoke()
    val albums = getAlbumsUseCase.invoke()

    val currentTrack = musicControllerRepository.currentTrack
    val playerState = musicControllerRepository.playerState

    fun onShowTrackFullScreen() = _showFullTrackScreen.tryEmit(true)
    fun onDismissTrackFullScreen() = _showFullTrackScreen.tryEmit(false)

    fun playATrack(trackId: Long) {
        musicControllerRepository.addMediaItems(allTracks.value)
        musicControllerRepository.play(trackId)
    }

    fun scanDevice() {
        // load all tracks
        viewModelScope.launch {
//            Timber.d("All tracks size: ${allTracks.size}")
//            //_allTracks.emit(allTracks)
//            _artistTracks.tryEmit(filterTracksByArtist(allTracks))
//            _albumTracks.tryEmit(filterTracksByAlbum(allTracks))
//
//            // Prepare to play all track
//            musicControllerRepository.addMediaItems(tracks = allTracks)
        }
    }

    fun onTrackEvent(event: TrackEvent) {
        when (event) {
            TrackEvent.Pause -> musicControllerRepository.pauseCurrentTrack()
            TrackEvent.Resume -> musicControllerRepository.resumeCurrentTrack()
            TrackEvent.SkipToNext -> musicControllerRepository.skipToNextTrack()
            TrackEvent.SkipToPrevious -> musicControllerRepository.skipToPreviousTrack()
            else -> {
                Timber.e("Event not handled: $event")
            }
        }
    }

    fun getAlbumArt(albumId: Long?, artistId: Long): Bitmap? {
        return getAlbumArtUseCase.invoke(albumId, artistId)
    }
}