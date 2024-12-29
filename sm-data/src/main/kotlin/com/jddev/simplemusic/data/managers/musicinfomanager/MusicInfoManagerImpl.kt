package com.jddev.simplemusic.data.managers.musicinfomanager

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MusicInfoManagerImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) : MusicInfoManager {

    private val albumArtGivenAlbumArtistCache = hashMapOf<Pair<Long, Long>, Bitmap?>()
    private val albumArtGivenArtistCache = hashMapOf<Long, Bitmap?>()

    // Handle broadcast receiver for Media Provider change here
    // handle queryAllTracks should be do only 1 time
    override fun queryAllTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        val contentResolver: ContentResolver = context.contentResolver
        val uri = MusicInfoStore.Media.EXTERNAL_CONTENT_URI
        val projection = DbUtils.TRACK_PROJECTION
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val isMusic =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.IS_MUSIC))
                if (isMusic != 1) continue
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.TITLE))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ARTIST))
                val artistId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ARTIST_ID))
                val album =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ALBUM))
                val albumId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ALBUM_ID))
                val data =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.DATA))
                val year =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.YEAR))

                //TODO: This make delay amount of time, should have better approach here
                val albumArt = DbUtils.getAlbumArt(data)
                if (albumArt != null) {
                    if (albumArtGivenAlbumArtistCache[Pair(albumId, artistId)] == null) {
                        albumArtGivenAlbumArtistCache[Pair(albumId, artistId)] = albumArt
                    }
                    if (albumArtGivenArtistCache[artistId] == null) {
                        albumArtGivenArtistCache[artistId] = albumArt
                    }
                }
                tracks.add(
                    Track(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        data = data,
                        year = year,
                        albumId = albumId,
                        artistId = artistId,
                    )
                )
            }
            cursor.close()
        }
        return tracks
    }

    override fun queryAllTracksAsync(): StateFlow<List<Track>> {
        val tracksStateFlow = MutableStateFlow<MutableList<Track>>(mutableListOf())
        coroutineScope.launch {
            val contentResolver: ContentResolver = context.contentResolver
            val uri = MusicInfoStore.Media.EXTERNAL_CONTENT_URI
            val projection = DbUtils.TRACK_PROJECTION
            val cursor = contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val isMusic =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.IS_MUSIC))
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media._ID))
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.TITLE))
                    if (isMusic != 1 ||
                        title.lowercase().startsWith("call recording") ||
                        title.lowercase().startsWith("ghi âm cuộc gọi")
                    ) continue
                    val artist =
                        cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ARTIST))
                    val artistId =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ARTIST_ID))
                    val album =
                        cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ALBUM))
                    val albumId =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.ALBUM_ID))
                    val data =
                        cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.DATA))
                    val year =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Media.YEAR))

                    //TODO: This make delay amount of time, should have better approach here
                    val albumArt = DbUtils.getAlbumArt(data)
                    if (albumArt != null) {
                        if (albumArtGivenAlbumArtistCache[Pair(albumId, artistId)] == null) {
                            albumArtGivenAlbumArtistCache[Pair(albumId, artistId)] = albumArt
                        }
                        if (albumArtGivenArtistCache[artistId] == null) {
                            albumArtGivenArtistCache[artistId] = albumArt
                        }
                    }

                    tracksStateFlow.update {
                        it.add(
                            Track(
                                id = id,
                                title = title,
                                artist = artist,
                                album = album,
                                data = data,
                                year = year,
                                albumId = albumId,
                                artistId = artistId,
                            )
                        )
                        it
                    }
                }
                cursor.close()
            }
        }
        return tracksStateFlow
    }

    override fun queryAlbums(): List<Album> {
        val albums = mutableListOf<Album>()
        val uri = MusicInfoStore.Albums.EXTERNAL_CONTENT_URI
        val projection = DbUtils.ALBUM_PROJECTION
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val albumId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Albums._ID))
                val albumName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Albums.ALBUM))
                val artistName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Albums.ARTIST))
                val artistId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Albums.ARTIST_ID))
                val numberOfSongs =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Albums.NUMBER_OF_SONGS))
                val numberOfSongsByArtist =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Albums.NUMBER_OF_SONGS_BY_ARTIST))
                albums.add(
                    Album(
                        id = albumId,
                        name = albumName,
                        artist = artistName,
                        numTracks = numberOfSongs,
                        numTracksByArtist = numberOfSongsByArtist,
                        artistId = artistId
                    )
                )
            }
            cursor.close()
        }
        return albums
    }

    override fun queryArtists(): List<Artist> {
        val artists = mutableListOf<Artist>()
        val uri = MusicInfoStore.Artists.EXTERNAL_CONTENT_URI
        val projection = DbUtils.ARTIST_PROJECTION
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val artistId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Artists._ID))
                val artistName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MusicInfoStore.Artists.ARTIST))
                val numberOfTracks =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Artists.NUMBER_OF_TRACKS))
                val numberOfAlbum =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MusicInfoStore.Artists.NUMBER_OF_ALBUMS))

                artists.add(
                    Artist(
                        id = artistId,
                        name = artistName,
                        numAlbums = numberOfAlbum,
                        numTracks = numberOfTracks
                    )
                )
            }
            cursor.close()
        }
        return artists
    }

    override fun getAlbumArt(albumId: Long, artistId: Long): Bitmap? {
        return albumArtGivenAlbumArtistCache[Pair(albumId, artistId)]
    }

    override fun getAlbumArt(artistId: Long): Bitmap? {
        return albumArtGivenArtistCache[artistId]
    }

    override fun queryTrackIdGivenAlbumId(albumId: Long): List<Long> {
        val trackIds: MutableList<Long> = mutableListOf()
        var cursor: Cursor? = null
        try {
            val projection = DbUtils.TRACK_PROJECTION
            cursor = context.contentResolver.query(
                MusicInfoStore.Media.EXTERNAL_CONTENT_URI,
                projection,
                MusicInfoStore.Media.ALBUM_ID + "=?",
                arrayOf(albumId.toString()),
                null
            )

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media._ID))
                    trackIds.add(id)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }
        return trackIds
    }

    override fun queryTrackIdGivenArtistId(artistId: Long): List<Long> {
        val trackIds: MutableList<Long> = mutableListOf()
        var cursor: Cursor? = null
        try {
            val projection = DbUtils.TRACK_PROJECTION
            cursor = context.contentResolver.query(
                MusicInfoStore.Media.EXTERNAL_CONTENT_URI,
                projection,
                MusicInfoStore.Media.ARTIST_ID + "=?",
                arrayOf(artistId.toString()),
                null
            )

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MusicInfoStore.Media._ID))
                    trackIds.add(id)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }
        return trackIds
    }
}