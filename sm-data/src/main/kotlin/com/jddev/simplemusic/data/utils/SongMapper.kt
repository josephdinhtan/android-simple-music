package com.jddev.simplemusic.data.utils

import androidx.media3.common.MediaItem
import com.jddev.simplemusic.data.datasource.remote.TrackDto
import com.jddev.simplemusic.domain.model.Track

fun TrackDto.toTrack() =
    Track(
        mediaId = mediaId,
        title = title,
        subtitle = subtitle,
        trackUrl = songUrl,
        imageUrl = imageUrl
    )

fun MediaItem.toTrack() =
    Track(
        mediaId = mediaId,
        title = mediaMetadata.title.toString(),
        subtitle = mediaMetadata.subtitle.toString(),
        trackUrl = mediaId,
        imageUrl = mediaMetadata.artworkUri.toString()
    )