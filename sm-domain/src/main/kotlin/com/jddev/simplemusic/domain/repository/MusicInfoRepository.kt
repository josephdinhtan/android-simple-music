package com.jddev.simplemusic.domain.repository

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.PlayList
import com.jddev.simplemusic.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MusicInfoRepository {
    val allTracks: StateFlow<List<Track>>
    val albums: StateFlow<List<Album>>
    val artists: StateFlow<List<Artist>>
    val playLists: StateFlow<List<PlayList>>

    fun initializer()
    fun getAlbum(albumId: Long): Album?
    fun getAlbumArt(albumId: Long?, artistId: Long): Bitmap?
    fun getArtist(artistId: Long): Artist?
    suspend fun getTrackGivenAlbumId(albumId: Long): List<Track>
    suspend fun getTrackGivenArtistId(artistId: Long): List<Track>
}