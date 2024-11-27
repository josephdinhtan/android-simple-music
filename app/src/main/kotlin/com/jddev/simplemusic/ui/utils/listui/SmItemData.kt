package com.jddev.simplemusic.ui.utils.listui

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.home.album.AlbumTrackGroup
import com.jddev.simplemusic.ui.home.artist.ArtistTrackGroup

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
            thumbnail = track.thumbnailBitmap,
        )
    }.toList()
}

fun artistTrackGroupsToSmItemList(list: List<ArtistTrackGroup>): List<SmItemData> {
    return list.map { artistTrackGroup ->
        SmItemData(
            title = artistTrackGroup.artist,
            subtitle = "${artistTrackGroup.tracks.size} tracks",
            thumbnail = artistTrackGroup.tracks.firstOrNull { it.thumbnailBitmap != null }?.thumbnailBitmap,
        )
    }.toList()
}

fun albumTrackGroupsToSmItemList(list: List<AlbumTrackGroup>): List<SmItemData> {
    return list.map { albumTrackGroup ->
        SmItemData(
            title = albumTrackGroup.album,
            subtitle = "${albumTrackGroup.tracks.size} tracks",
            thumbnail = albumTrackGroup.tracks.firstOrNull { it.thumbnailBitmap != null }?.thumbnailBitmap,
        )
    }.toList()
}