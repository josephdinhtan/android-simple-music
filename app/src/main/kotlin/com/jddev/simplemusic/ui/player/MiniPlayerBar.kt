package com.jddev.simplemusic.ui.player

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.R
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.components.BlurBackgroundImage
import com.jddev.simplemusic.ui.components.ThumbnailImage
import com.jddev.simplemusic.ui.components.TrackEvent
import com.jddev.simplemusic.ui.utils.getTestTrack
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun MiniPlayerBar(
    modifier: Modifier = Modifier,
    track: Track,
    getAlbumArt: (Long?, Long) -> Bitmap?,
    onTrackEvent: (TrackEvent) -> Unit,
    playerState: PlayerState?,
    onBarClick: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
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
        val imageBitmap = getAlbumArt(track.albumId, track.artistId)
        BlurBackgroundImage(
            modifier = Modifier.fillMaxSize(), imageBitmap = imageBitmap
        )
        TrackBottomControllerContent(
            track = track,
            imageBitmap = imageBitmap,
            onEvent = onTrackEvent,
            playerState = playerState,
        )
    }
}


@Composable
private fun TrackBottomControllerContent(
    track: Track,
    imageBitmap: Bitmap?,
    onEvent: (TrackEvent) -> Unit,
    playerState: PlayerState?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        ThumbnailImage(
            modifier = Modifier
                .size(64.dp)
                .padding(start = 16.dp)
                .padding(vertical = 8.dp),
            imageBitmap = imageBitmap
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 8.dp),
        ) {
            Text(
                track.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W700),
                textAlign = TextAlign.Center,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
            )

            Text(track.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee()
                    .graphicsLayer {
                        alpha = 0.90f
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
        MiniPlayerBar(onTrackEvent = {},
            playerState = PlayerState.PLAYING,
            track = Track.getTestTrack(),
            onBarClick = {},
            getAlbumArt = { _, _ -> null })
    }
}