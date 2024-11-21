package com.jddev.simplemusic.data.datasource.remote

import android.graphics.Bitmap

data class TrackDto(
    val mediaId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val songUrl: String = "",
    val artworkImage: Bitmap? = null
)