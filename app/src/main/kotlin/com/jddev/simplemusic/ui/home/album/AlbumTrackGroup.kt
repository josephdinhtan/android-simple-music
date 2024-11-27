package com.jddev.simplemusic.ui.home.album

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Track

data class AlbumTrackGroup(
    val album: String,
    val thumbnailBitmap: Bitmap? = null,
    val tracks: List<Track>,
)
