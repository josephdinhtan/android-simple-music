package com.jddev.simplemusic.ui.home.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicInfoRepository: MusicInfoRepository,
    private val musicControllerRepository: MusicControllerRepository,
) : ViewModel() {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    private val _album = MutableStateFlow<Album?>(null)
    val album = _album.asStateFlow()

    fun queryTracksGivenAlbum(albumId: Long) {
        _tracks.tryEmit(emptyList())
        viewModelScope.launch {
            _album.value = musicInfoRepository.getAlbum(albumId = albumId)
            _tracks.value = musicInfoRepository.getTrackGivenAlbum(albumId = albumId)
        }
    }

    fun playATrack(trackId: Long) {
        musicControllerRepository.addMediaItems(tracks.value)
        musicControllerRepository.play(trackId)
    }
}