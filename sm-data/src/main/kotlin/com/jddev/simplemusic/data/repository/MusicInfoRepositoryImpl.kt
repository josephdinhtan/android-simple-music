package com.jddev.simplemusic.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.jddev.simplemusic.data.managers.musicinfomanager.MusicInfoManager
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

    private var _allTracks = MutableStateFlow<List<Track>>(emptyList())
    override var allTracks = _allTracks.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    override val albums = _albums.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    override val artists = _artists.asStateFlow()

    private val _playlists = MutableStateFlow<List<PlayList>>(emptyList())
    override val playLists = _playlists.asStateFlow()

    override fun initializer() {
        coroutineScope.launch {
            allTracks = musicInfoManager.queryAllTracksAsync()
            _artists.value = musicInfoManager.queryArtists()
            _albums.value = musicInfoManager.queryAlbums()
        }
    }

    override fun getAlbum(albumId: Long): Album? {
        return albums.value.firstOrNull { it.id == albumId }
    }

    override fun getAlbumArt(albumId: Long?, artistId: Long): Bitmap? {
        return if (albumId == null) {
            musicInfoManager.getAlbumArt(artistId)
        } else {
            musicInfoManager.getAlbumArt(albumId, artistId)
        }
    }

    override fun getArtist(artistId: Long): Artist? {
        return artists.value.firstOrNull { it.id == artistId }
    }

    override suspend fun getTrackGivenAlbumId(albumId: Long): List<Track> {
        val trackIds = musicInfoManager.queryTrackIdGivenAlbumId(albumId)
        val res = allTracks.value.filter { track -> track.id in trackIds }.toList()
        return res
    }

    override suspend fun getTrackGivenArtistId(artistId: Long): List<Track> {
        val trackIds = musicInfoManager.queryTrackIdGivenArtistId(artistId)
        val res = allTracks.value.filter { track -> track.id in trackIds }.toList()
        return res
    }
}