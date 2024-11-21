package com.jddev.simplemusic.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.R
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun TrackBottomBar(
    modifier: Modifier = Modifier,
    onTrackEvent: (TrackEvent) -> Unit,
    playerState: PlayerState?,
    track: Track?,
    onBarClick: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    AnimatedVisibility(
        visible = track != null, modifier = modifier
    ) {
        if (track != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
                    .pointerInput(Unit) {
                        detectDragGestures(onDragEnd = {
                            when {
                                offsetX > 0 -> {
                                    onTrackEvent(TrackEvent.SkipToPrevious)
                                }

                                offsetX < 0 -> {
                                    onTrackEvent(TrackEvent.SkipToNext)
                                }
                            }
                        }, onDrag = { change, dragAmount ->
                            change.consume()
                            val (x, _) = dragAmount
                            offsetX = x
                        })

                    }
                    .background(
                        if (!isSystemInDarkTheme()) {
                            Color.LightGray
                        } else Color.DarkGray
                    )
                    .clickable(onClick = { onBarClick() }),
            ) {
                TrackBottomControllerContent(
                    track = track,
                    onEvent = onTrackEvent,
                    playerState = playerState,
                )
            }
        }
    }
}


@Composable
private fun TrackBottomControllerContent(
    track: Track,
    onEvent: (TrackEvent) -> Unit,
    playerState: PlayerState?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (track.thumbnailBitmap != null)
            ThumbnailImage(modifier = Modifier.padding(8.dp), imageBitmap = track.thumbnailBitmap!!)
        else
            ThumbnailImage(modifier = Modifier.padding(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 8.dp),
        ) {
            Text(
                track.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(track.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.graphicsLayer {
                    alpha = 0.60f
                })
        }
        val painter = painterResource(
            if (playerState == PlayerState.PLAYING) {
                R.drawable.ic_round_pause
            } else {
                R.drawable.ic_round_play_arrow
            }
        )

        Image(
            painter = painter,
            contentDescription = "Music",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(48.dp)
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    }, indication = null
                ) {
                    if (playerState == PlayerState.PLAYING) {
                        onEvent(TrackEvent.Pause)
                    } else {
                        onEvent(TrackEvent.Resume)
                    }
                },
        )
    }
}

@Composable
@Preview
private fun Preview() {
    StUiPreview(modifier = Modifier.background(Color.Green.copy(alpha = 0.3f))) {
        TrackBottomBar(modifier = Modifier.padding(16.dp),
            onTrackEvent = {},
            playerState = PlayerState.PLAYING,
            track = Track(
                title = "Title", subtitle = "Subtitle", trackUrl = "", id = "0"
            ),
            onBarClick = {})
    }
}