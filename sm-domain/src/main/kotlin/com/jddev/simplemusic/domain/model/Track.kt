package com.jddev.simplemusic.domain.model

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.utils.getFileThumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Track(
    val id: String,
    val title: String,
    val subtitle: String,
    val trackUrl: String,
) {
    var thumbnailBitmap: Bitmap? = null
        private set
    init {
        CoroutineScope(Dispatchers.IO).launch {
            if(thumbnailBitmap == null) {
                thumbnailBitmap = getFileThumbnail(trackUrl)
            }
        }
    }
}