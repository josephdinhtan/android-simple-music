package com.jddev.simplemusic.ui.utils.listui

import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track

data class SmItemData(
    val title: String,
    val subtitle: String,
    val albumId: Long?,
    val artistId: Long,
)

fun trackGroupsToSmItemList(list: List<Track>): List<SmItemData> {
    return list.map { track ->
        SmItemData(
            title = track.title,
            subtitle = track.artist,
            albumId = track.albumId,
            artistId = track.artistId,
        )
    }.toList()
}

fun artistTrackGroupsToSmItemList(list: List<Artist>): List<SmItemData> {
    return list.map { artist ->
        SmItemData(
            title = artist.name,
            subtitle = "${artist.numAlbums} tracks",
            albumId = null,
            artistId = artist.id,
        )
    }.toList()
}

fun albumTrackGroupsToSmItemList(list: List<Album>): List<SmItemData> {
    return list.map { album ->
        SmItemData(
            title = album.name,
            subtitle = "${album.numTracks} tracks",
            albumId = album.id,
            artistId = album.artistId,
        )
    }.toList()
}