package com.jddev.simplemusic.domain.model

data class Album(
    val id: Long,
    val name: String,
    val artist: String,
    val numTracks: Int,
    val numTracksByArtist: Int,
    val artistId: Long,
) {
    // for companion extension
    companion object
}
