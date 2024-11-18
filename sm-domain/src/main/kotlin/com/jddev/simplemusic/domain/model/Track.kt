package com.jddev.simplemusic.domain.model

data class Track(
    val mediaId: String,
    val title: String,
    val subtitle: String,
    val trackUrl: String,
    val imageUrl: String
)