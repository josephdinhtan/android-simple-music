package com.jddev.simplemusic.domain.model

import android.graphics.Bitmap

data class Album(
    val id: Long,
    val name: String,
    val artist: String,
    var albumArt: Bitmap?,
    val numberOfTracks: Int,
)
