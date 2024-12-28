package com.jddev.simplemusic.data.managers.musicinfomanager

import android.net.Uri
import android.provider.MediaStore

internal object MusicInfoStore {

    object Media {
        val EXTERNAL_CONTENT_URI: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        const val _ID = MediaStore.Audio.Media._ID
        const val IS_MUSIC = MediaStore.Audio.Media.IS_MUSIC
        const val TITLE = MediaStore.Audio.Media.TITLE
        const val ARTIST = MediaStore.Audio.Media.ARTIST
        const val ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID
        const val ALBUM = MediaStore.Audio.Media.ALBUM
        const val ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID
        const val DURATION = MediaStore.Audio.Media.DURATION
        const val TRACK = MediaStore.Audio.Media.TRACK
        const val DATA = MediaStore.Audio.Media.DATA
        const val YEAR = MediaStore.Audio.AudioColumns.YEAR
    }

    object Albums {
        val EXTERNAL_CONTENT_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

        const val _ID = MediaStore.Audio.Albums._ID
        const val ALBUM = MediaStore.Audio.Albums.ALBUM
        const val ARTIST = MediaStore.Audio.Albums.ARTIST
        const val ARTIST_ID = MediaStore.Audio.Albums.ARTIST_ID
        const val NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS
        const val NUMBER_OF_SONGS_BY_ARTIST = MediaStore.Audio.Albums.NUMBER_OF_SONGS_FOR_ARTIST
    }

    object Artists {
        val EXTERNAL_CONTENT_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI

        const val _ID = MediaStore.Audio.Artists._ID
        const val ARTIST = MediaStore.Audio.Artists.ARTIST
        const val NUMBER_OF_TRACKS = MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        const val NUMBER_OF_ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    }
}