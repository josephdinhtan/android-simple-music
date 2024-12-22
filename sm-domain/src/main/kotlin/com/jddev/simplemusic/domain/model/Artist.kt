package com.jddev.simplemusic.domain.model

data class Artist (
    val id: Long,
    val name: String,
    val numberOfAlbum: Int,
    val albumArt: String,
)