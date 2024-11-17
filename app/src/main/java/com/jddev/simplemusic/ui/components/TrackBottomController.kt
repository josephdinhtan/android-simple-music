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
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun TrackBottomController(
    modifier: Modifier = Modifier,
    onEvent: (TrackEvent) -> Unit,
    playerState: PlayerState?,
    track: Track?,
    onBarClick: (trackId: String) -> Unit
) {

    var offsetX by remember { mutableFloatStateOf(0f) }

    AnimatedVisibility(
        visible = playerState != PlayerState.NONE, modifier = modifier
    ) {
        if (track != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .clickable(onClick = { onBarClick() })
                    .clip(shape = MaterialTheme.shapes.medium)
                    .pointerInput(Unit) {
                        detectDragGestures(onDragEnd = {
                            when {
                                offsetX > 0 -> {
                                    onEvent(TrackEvent.SkipToPrevious)
                                }

                                offsetX < 0 -> {
                                    onEvent(TrackEvent.SkipToNext)
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
                    ),
            ) {
                TrackBottomControllerContent(
                    track = track,
                    onEvent = onEvent,
                    playerState = playerState,
                    onBarClick = onBarClick
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
    onBarClick: (trackId: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onBarClick(track.mediaId) })
    ) {
        Image(
//                painter = rememberAsyncImagePainter(song.imageUrl),
            painter = painterResource(id = R.drawable.song_img),
            contentDescription = track.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
                .clip(shape = MaterialTheme.shapes.medium)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 32.dp),
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
        TrackBottomController(modifier = Modifier.padding(16.dp),
            onEvent = {},
            playerState = PlayerState.PLAYING,
            track = Track(
                title = "Title", subtitle = "Subtitle", imageUrl = "", songUrl = "", mediaId = "0"
            ),
            onBarClick = {})
    }
}