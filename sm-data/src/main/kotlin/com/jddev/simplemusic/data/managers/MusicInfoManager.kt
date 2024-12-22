package com.jddev.simplemusic.data.managers

import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track


interface MusicInfoManager {
    fun queryAllTracks() : List<Track>
    fun queryAlbums(): List<Album>
    fun queryArtists(): List<Artist>
    fun queryTracksGivenAlbum(albumId: Long): List<Track>
}