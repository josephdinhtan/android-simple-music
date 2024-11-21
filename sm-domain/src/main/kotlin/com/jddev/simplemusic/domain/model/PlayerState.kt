package com.jddev.simplemusic.domain.model

enum class PlayerState {
    NO_TRACK, // No track selected
    PLAYING, // a track is playing
    PAUSED, // a track is paused
    STOPPED, // a track is stopped
}