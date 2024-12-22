package com.jddev.simplemusic.ui.utils.listui

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track

data class SmItemData(
    val title: String,
    val subtitle: String,
    val thumbnail: Bitmap? = null,
)

fun trackGroupsToSmItemList(list: List<Track>): List<SmItemData> {
    return list.map { track ->
        SmItemData(
            title = track.title,
            subtitle = track.artist,
            thumbnail = track.albumArt,
        )
    }.toList()
}

fun artistTrackGroupsToSmItemList(list: List<Artist>): List<SmItemData> {
    return list.map { artist ->
        SmItemData(
            title = artist.name,
            subtitle = "${artist.numberOfAlbum} tracks",
            thumbnail = null//artist.tracks.firstOrNull { it.thumbnailBitmap != null }?.thumbnailBitmap,
        )
    }.toList()
}

fun albumTrackGroupsToSmItemList(list: List<Album>): List<SmItemData> {
    return list.map { album ->
        SmItemData(
            title = album.name,
            subtitle = "${album.numberOfTracks} tracks",
            thumbnail = album.albumArt,
        )
    }.toList()
}