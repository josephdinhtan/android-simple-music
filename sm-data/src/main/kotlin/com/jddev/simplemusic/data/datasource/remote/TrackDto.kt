package com.jddev.simplemusic.data.datasource.remote

data class TrackDto(
    val mediaId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val songUrl: String = "",
    val imageUrl: String = ""
)