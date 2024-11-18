package com.jddev.simplemusic.domain.model

enum class PlayerState {
    NONE, // No track selected
    PLAYING, // a track is playing
    PAUSED, // a track is paused
    STOPPED, // a track is stopped
}