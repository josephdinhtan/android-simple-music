package com.jddev.simplemusic.ui.library.artist

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import com.jddev.simplemusic.domain.repository.PlaybackRepository
import com.jddev.simplemusic.domain.usecase.GetAlbumArtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val getAlbumArtUseCase: GetAlbumArtUseCase,
    private val musicInfoRepository: MusicInfoRepository,
    private val musicControllerRepository: PlaybackRepository,
) : ViewModel() {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist = _artist.asStateFlow()

    fun queryTracksGivenArtist(artistId: Long) {
        _tracks.tryEmit(emptyList())
        viewModelScope.launch {
            _artist.value = musicInfoRepository.getArtist(artistId = artistId)
            _tracks.value = musicInfoRepository.getTrackGivenArtistId(artistId = artistId)
        }
    }

    fun getAlbumArt(albumId: Long?, artistId: Long): Bitmap? {
        return getAlbumArtUseCase.invoke(albumId, artistId)
    }

    fun playATrack(trackId: Long) {
        musicControllerRepository.addMediaItems(tracks.value)
        musicControllerRepository.play(trackId)
    }
}