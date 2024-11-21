package com.jddev.simplemusic.data.utils

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.jddev.simplemusic.domain.model.Track

internal fun getAllAudioFiles(context: Context): List<Track> {
    val musicFiles = mutableListOf<Track>()
    val contentResolver: ContentResolver = context.contentResolver
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA
    )
    val cursor = contentResolver.query(uri, projection, null, null, null)
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            musicFiles.add(
                Track(
                    id = id.toString(),
                    title = title,
                    subtitle = artist,
                    trackUrl = data,
                )
            )
        }
        cursor.close()
    }
    return musicFiles
}