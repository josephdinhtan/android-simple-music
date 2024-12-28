package com.jddev.simplemusic.data.managers.musicinfomanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

internal object DbUtils {

    val TRACK_PROJECTION = arrayOf(
        MusicInfoStore.Media.IS_MUSIC,
        MusicInfoStore.Media._ID,
        MusicInfoStore.Media.TITLE,
        MusicInfoStore.Media.ARTIST,
        MusicInfoStore.Media.ARTIST_ID,
        MusicInfoStore.Media.ALBUM,
        MusicInfoStore.Media.ALBUM_ID,
        MusicInfoStore.Media.DATA,
        MusicInfoStore.Media.DURATION,
        MusicInfoStore.Media.YEAR,
        MusicInfoStore.Media.TRACK
    )

    val ALBUM_PROJECTION = arrayOf(
        MusicInfoStore.Albums._ID,
        MusicInfoStore.Albums.ALBUM,
        MusicInfoStore.Albums.ARTIST,
        MusicInfoStore.Albums.ARTIST_ID,
        MusicInfoStore.Albums.NUMBER_OF_SONGS,
        MusicInfoStore.Albums.NUMBER_OF_SONGS_BY_ARTIST
    )

    val ARTIST_PROJECTION = arrayOf(
        MusicInfoStore.Artists._ID,
        MusicInfoStore.Artists.ARTIST,
        MusicInfoStore.Artists.NUMBER_OF_TRACKS,
        MusicInfoStore.Artists.NUMBER_OF_ALBUMS,
    )

    fun getAlbumArt(path: String?): Bitmap? {
        try {
            if (path == null) return null
            val mr = MediaMetadataRetriever()
            mr.setDataSource(path)
            val byte1 = mr.embeddedPicture
            mr.release()
            return if (byte1 != null) BitmapFactory.decodeByteArray(byte1, 0, byte1.size)
            else null
        } catch (e: Exception) {
            return null
        }
    }
}