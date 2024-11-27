package com.jddev.simplemusic.ui.home.artist

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.model.Track

data class ArtistTrackGroup(
    val artist: String,
    val thumbnailBitmap: Bitmap? = null,
    val tracks: List<Track>,
)
