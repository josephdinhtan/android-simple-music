package com.jddev.simplemusic.data.managers

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.provider.MediaStore
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track
import timber.log.Timber


class MusicInfoManagerImpl(val context: Context) : MusicInfoManager {

    override fun queryAllTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        val contentResolver: ContentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
        )
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val album =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val data =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                tracks.add(
                    Track(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        trackUrl = data,
                    )
                )
            }
            cursor.close()
        }
        return tracks
    }

    override fun queryAlbums(): List<Album> {
        val albums = mutableListOf<Album>()
        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val albumId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
                val albumName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM))
                val artistName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
                val numberOfSongs =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
                val albumArt = getAlbumArt(albumId)
                if(albumArt != null) {
                    Timber.e("JDdev AlbumArt != null")
                } else {
                    Timber.e("JDdev AlbumArt == null")
                }
                albums.add(Album(albumId, albumName, artistName, albumArt, numberOfSongs))
            }
            cursor.close()
        }
        return albums
    }

    override fun queryArtists(): List<Artist> {
        val artists = mutableListOf<Artist>()
        val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ARTIST,
        )
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val artistId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID))
                val artistName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))

                artists.add(Artist(artistId, artistName, 0, ""))
            }
            cursor.close()
        }
        return artists
    }

    override fun queryTracksGivenAlbum(albumId: Long): List<Track> {
        val tracks: MutableList<Track> = ArrayList()
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.DATA,
            )
            cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Audio.Media.ALBUM_ID + "=?",
                arrayOf(albumId.toString()),
                null
            )

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val album =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val data =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    tracks.add(
                        Track(
                            id = id,
                            title = title,
                            artist = artist,
                            album = album,
                            trackUrl = data,
                        )
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }
        return tracks
    }

    private fun getAlbumArt(albumId: Long): Bitmap? {
        // TODO: get AlbumArt here
        return null
    }
}