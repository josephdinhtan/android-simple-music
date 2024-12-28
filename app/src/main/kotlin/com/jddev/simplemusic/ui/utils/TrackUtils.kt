package com.jddev.simplemusic.ui.utils

import com.jddev.simplemusic.domain.model.Track

fun Track.Companion.getTestTrack(): Track {
    return Track(
        id = 0,
        title = "Test Title",
        album = "Test Album",
        artist = "Test Artist",
        data = "Test Track Url",
        year = 1,
        albumId = 1,
        artistId = 1,
    )
}