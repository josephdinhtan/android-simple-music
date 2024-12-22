package com.jddev.simplemusic.domain.model

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.utils.getFileThumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Track(
    val id: Long,
    val title: String,
    val album: String,
    val artist: String,
    val trackUrl: String,
) {
    var albumArt: Bitmap? = null
        private set

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if(albumArt == null) {
                albumArt = getFileThumbnail(trackUrl)
            }
        }
    }

    companion object
}