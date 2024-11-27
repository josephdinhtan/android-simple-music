package com.jddev.simplemusic.data.utils

import androidx.media3.common.MediaItem
import com.jddev.simplemusic.domain.model.Track

fun MediaItem.toTrack() =
    Track(
        id = mediaId,
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.subtitle.toString(),
        album = mediaMetadata.albumTitle.toString(),
        trackUrl = mediaId,
    )