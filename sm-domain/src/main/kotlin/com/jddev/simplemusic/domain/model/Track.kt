package com.jddev.simplemusic.domain.model

data class Track(
    val id: Long,
    val title: String,
    val album: String,
    val artist: String,
    val data: String,
    val year: Int,

    val artistId: Long,
    val albumId: Long,
) {
    // for companion extension
    companion object
}