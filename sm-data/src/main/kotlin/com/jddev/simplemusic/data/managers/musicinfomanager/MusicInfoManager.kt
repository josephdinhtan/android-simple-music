package com.jddev.simplemusic.data.managers.musicinfomanager

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MusicInfoManager {
    fun queryAllTracks() : List<Track>
    fun queryAllTracksAsync() : StateFlow<List<Track>>
    fun queryAlbums(): List<Album>
    fun queryArtists(): List<Artist>
    fun getAlbumArt(albumId: Long, artistId: Long) : Bitmap?
    fun getAlbumArt(artistId: Long) : Bitmap?
    fun queryTrackIdGivenAlbumId(albumId: Long): List<Long>
    fun queryTrackIdGivenArtistId(artistId: Long): List<Long>
}