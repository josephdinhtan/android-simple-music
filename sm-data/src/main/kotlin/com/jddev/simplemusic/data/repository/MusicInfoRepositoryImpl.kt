package com.jddev.simplemusic.data.repository

import android.content.Context
import com.jddev.simplemusic.data.managers.MusicInfoManager
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.PlayList
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicInfoRepositoryImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val musicInfoManager: MusicInfoManager,
) : MusicInfoRepository {

    private val _allTracks = MutableStateFlow<List<Track>>(emptyList())
    override val allTracks = _allTracks.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    override val albums = _albums.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    override val artists = _artists.asStateFlow()

    private val _playlists = MutableStateFlow<List<PlayList>>(emptyList())
    override val playLists = _playlists.asStateFlow()

    override fun initializer() {
        coroutineScope.launch {
            _allTracks.value = musicInfoManager.queryAllTracks()
            _albums.value = musicInfoManager.queryAlbums()
            _artists.value = musicInfoManager.queryArtists()
        }
    }

    override fun getAlbum(albumId: Long): Album? {
        return _albums.value.firstOrNull{it.id == albumId}
    }

    override suspend fun getTrackGivenAlbum(albumId: Long): List<Track> {
        return musicInfoManager.queryTracksGivenAlbum(albumId)
    }
}