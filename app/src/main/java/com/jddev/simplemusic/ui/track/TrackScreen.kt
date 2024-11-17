package com.jddev.simplemusic.ui.track

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.R
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.MusicControllerUiState
import com.jddev.simplemusic.ui.components.AnimatedVinyl
import com.jddev.simplemusic.ui.components.LineMusicSlider
import com.jddev.simplemusic.ui.components.PlayerState
import com.jddev.simplemusic.ui.components.TrackEvent
import com.jddev.simplemusic.updatest.StUiPreview
import com.jddev.simplemusic.updatest.toTime
import timber.log.Timber

@Composable
fun TrackScreen(
    onEvent: (TrackEvent) -> Unit,
    musicControllerUiState: MusicControllerUiState,
    onBack: () -> Unit,
) {
    if (musicControllerUiState.currentTrack != null) {
        TrackScreenBody(
            song = musicControllerUiState.currentTrack,
            onNavigateUp = onBack,
            musicControllerUiState = musicControllerUiState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackScreenBody(
    song: Track,
    onEvent: (TrackEvent) -> Unit,
    musicControllerUiState: MusicControllerUiState,
    onNavigateUp: () -> Unit,
) {
//    val swipeAbleState = remember {
//        AnchoredDraggableState(
//            initialValue = 0,
//        )
//    }
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0, endAnchor to 1
    )

    val backgroundColor = MaterialTheme.colorScheme.background

    val dominantColor by remember { mutableStateOf(Color.Transparent) }

    val context = LocalContext.current

//    val imagePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(context).data(song.imageUrl).crossfade(true).build()
//    )
    val imagePainter = painterResource(R.drawable.song_img)

    val iconResId =
        if (musicControllerUiState.playerState == PlayerState.PLAYING) R.drawable.ic_round_pause else R.drawable.ic_round_play_arrow

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
//            .anchoredDraggable(
//                state = swipeAbleState,
////                anchors = anchors,
////                thresholds = { _, _ -> FractionalThreshold(0.34f) },
//                orientation = Orientation.Vertical,
//                enabled = true
//            )
    ) {
//        if (swipeAbleState.currentValue >= 1) {
//            LaunchedEffect(key1 = Unit) {
//                onNavigateUp()
//            }
//        }
        TrackScreenContent(song = song,
            isTrackPlaying = musicControllerUiState.playerState == PlayerState.PLAYING,
            imagePainter = imagePainter,
            dominantColor = dominantColor,
            currentTime = musicControllerUiState.currentPosition,
            totalTime = musicControllerUiState.totalDuration,
            playPauseIcon = iconResId,
            playOrToggleTrack = {
                onEvent(if (musicControllerUiState.playerState == PlayerState.PLAYING) TrackEvent.Pause else TrackEvent.Resume)
            },
            playNextTrack = { onEvent(TrackEvent.SkipToNext) },
            playPreviousTrack = { onEvent(TrackEvent.SkipToPrevious) },
            onSliderChange = { newPosition ->
                onEvent(TrackEvent.SeekTrackToPosition(newPosition.toLong()))
            },
            onForward = {
                onEvent(TrackEvent.SeekTrackToPosition(musicControllerUiState.currentPosition + 10 * 1000))
            },
            onRewind = {
                musicControllerUiState.currentPosition.let { currentPosition ->
                    onEvent(TrackEvent.SeekTrackToPosition(if (currentPosition - 10 * 1000 < 0) 0 else currentPosition - 10 * 1000))
                }
            },
            onClose = { onNavigateUp() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreenContent(
    song: Track,
    isTrackPlaying: Boolean,
    imagePainter: Painter,
    dominantColor: Color,
    currentTime: Long,
    totalTime: Long,
    @DrawableRes playPauseIcon: Int,
    playOrToggleTrack: () -> Unit,
    playNextTrack: () -> Unit,
    playPreviousTrack: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onClose: () -> Unit
) {
    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(
            dominantColor, MaterialTheme.colorScheme.background
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background
        )
    }

    Box(
        modifier = Modifier
//                .background(
//                    Brush.verticalGradient(
//                        colors = gradientColors,
//                        endY = LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
//                    )
//                )
            .fillMaxSize()
//                .systemBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 50.dp),
            painter = painterResource(id = R.drawable.song_img),
            contentScale = ContentScale.FillBounds,
            contentDescription = "blur Background"
        )
        Column {
            IconButton(
                onClick = onClose
            ) {
                Image(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Close",
//                        colorFilter = ColorFilter.tint(LocalContentColor.current)
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .weight(1f, fill = false)
                        .aspectRatio(1f)

                ) {
                    AnimatedVinyl(painter = imagePainter, isTrackPlaying = isTrackPlaying)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(song.subtitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.graphicsLayer {
                            alpha = 0.60f
                        })
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    LineMusicSlider(
                        modifier = Modifier.fillMaxWidth(),
                        currentTime = currentTime,
                        totalTime = totalTime,
                        onSliderChange = onSliderChange
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = currentTime.toTime(),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
//                                }
//                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Timber.e("totalTime: $totalTime")
                        Timber.e("totalTime.toTime(): ${totalTime.toTime()}")
                        Text(
                            totalTime.toTime(),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
//                                }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "Skip Previous",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = playPreviousTrack)
                            .padding(12.dp)
                            .size(32.dp),
                        tint = Color.White
                    )
                    Icon(
                        imageVector = Icons.Rounded.Replay10,
                        contentDescription = "Replay 10 seconds",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onRewind)
                            .padding(12.dp)
                            .size(32.dp),
                        tint = Color.White
                    )
                    Icon(
                        painter = painterResource(playPauseIcon),
                        contentDescription = "Play",
                        modifier = Modifier
                            .clip(CircleShape)
//                            .background(Color.White.copy(alpha = 0.8f))
                            .clickable(onClick = playOrToggleTrack)
                            .size(64.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.8f),
                                shape = CircleShape
                            )
                            .padding(8.dp),

                        tint = Color.White
                    )
                    Icon(
                        imageVector = Icons.Rounded.Forward10,
                        contentDescription = "Forward 10 seconds",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onForward)
                            .padding(12.dp)
                            .size(32.dp),
                        tint = Color.White
                    )
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "Skip Next",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = playNextTrack)
                            .padding(12.dp)
                            .size(32.dp),
                        tint = Color.White
                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun Preview() {
    StUiPreview {
        TrackScreen(
            onEvent = {},
            musicControllerUiState = MusicControllerUiState(
                playerState = PlayerState.PLAYING,
                currentTrack = Track("Test Track", "Test Track", "Test single", "", ""),
                currentPosition = 20L,
                totalDuration = 100L,
            ),
            onBack = {})
    }
}