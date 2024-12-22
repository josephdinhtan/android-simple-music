package com.jddev.simplemusic.ui.utils

import com.jddev.simplemusic.domain.model.Track

fun Track.Companion.getTestTrack(): Track {
    return Track(
        id = 0,
        title = "Test Titlte",
        album = "Test Album",
        artist = "Test Artist",
        trackUrl = "Test Track Url",
    )
}