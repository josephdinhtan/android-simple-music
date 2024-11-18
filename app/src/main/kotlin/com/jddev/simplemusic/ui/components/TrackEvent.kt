package com.jddev.simplemusic.ui.components

import com.jddev.simplemusic.domain.model.Track

sealed class TrackEvent {
    data object Play : TrackEvent()
    data object Pause : TrackEvent()
    data object Resume : TrackEvent()
    data object Fetch : TrackEvent()
    data object SkipToNext : TrackEvent()
    data object SkipToPrevious : TrackEvent()
    data class SeekTrackToPosition(val position: Long) : TrackEvent()
    data class OnSongSelected(val selectedTrack: Track) : TrackEvent()
}