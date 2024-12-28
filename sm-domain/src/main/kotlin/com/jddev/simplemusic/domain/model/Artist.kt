package com.jddev.simplemusic.domain.model

data class Artist (
    val id: Long,
    val name: String,
    val numAlbums: Int,
    val numTracks: Int
) {
    // for companion extension
    companion object
}