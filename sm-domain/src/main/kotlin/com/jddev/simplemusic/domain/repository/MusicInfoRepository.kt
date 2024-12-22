package com.jddev.simplemusic.domain.repository

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
    suspend fun getTrackGivenAlbum(albumId: Long): List<Track>
}