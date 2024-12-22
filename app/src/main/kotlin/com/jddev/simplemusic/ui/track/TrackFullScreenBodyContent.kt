package com.jddev.simplemusic.ui.track

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.R
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.MusicControllerUiState
import com.jddev.simplemusic.ui.components.AnimatedVinyl
import com.jddev.simplemusic.ui.components.LineMusicSlider
import com.jddev.simplemusic.ui.components.TrackEvent
import com.jddev.simplemusic.ui.utils.getTestTrack
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun TrackFullScreenBodyContent(
    onEvent: (TrackEvent) -> Unit,
    musicControllerUiState: MusicControllerUiState,
    onShowMenu: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val iconResId =
        if (musicControllerUiState.playerState == PlayerState.PLAYING) R.drawable.ic_round_pause else R.drawable.ic_round_play_arrow

    TrackScreenContent(
        track = musicControllerUiState.currentTrack,
        isTrackPlaying = musicControllerUiState.playerState == PlayerState.PLAYING,
        imageBitmap = musicControllerUiState.currentTrack.albumArt,
        currentTime = musicControllerUiState.currentPosition,
        totalTime = musicControllerUiState.totalDuration,
        playPauseIcon = iconResId,
        onShowMenu = onShowMenu,
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

@Composable
fun TrackScreenContent(
    track: Track,
    isTrackPlaying: Boolean,
    imageBitmap: Bitmap?,
    currentTime: Long,
    totalTime: Long,
    @DrawableRes playPauseIcon: Int,
    playOrToggleTrack: () -> Unit,
    playNextTrack: () -> Unit,
    playPreviousTrack: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onShowMenu: () -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ColumnPortraitRowLandscape(modifier = Modifier.fillMaxSize(), contentRow = {
            Column(Modifier.fillMaxHeight()) {
                with(LocalDensity.current) {
                    Spacer(
                        modifier = Modifier.height(
                            WindowInsets.statusBars.getTop(this).toDp()
                        )
                    )
                }
                IconButton(
                    onClick = onClose
                ) {
                    Image(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Close",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
            AnimatedVinyl(
                modifier = Modifier
                    .weight(0.5f)
                    .aspectRatio(1f, true)
                    .padding(16.dp),
                bitmap = imageBitmap,
                painter = painterResource(R.drawable.song_img),
                isTrackPlaying = isTrackPlaying
            )
            ControllerPart(
                modifier = Modifier.weight(0.5f),
                track = track,
                currentTime = currentTime,
                totalTime = totalTime,
                playPauseIcon = playPauseIcon,
                playOrToggleTrack = playOrToggleTrack,
                playPreviousTrack = playPreviousTrack,
                onSliderChange = onSliderChange,
                onRewind = onRewind,
                onForward = onForward,
                playNextTrack = playNextTrack,
                onShowMenu = onShowMenu
            )
        }, contentColumn = {
            with(LocalDensity.current) {
                Spacer(modifier = Modifier.height(WindowInsets.statusBars.getTop(this).toDp()))
            }
            Box(Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onClose
                ) {
                    Image(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Close",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
            AnimatedVinyl(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(16.dp),
                bitmap = imageBitmap,
                painter = painterResource(R.drawable.song_img),
                isTrackPlaying = isTrackPlaying
            )
            ControllerPart(
                modifier = Modifier.weight(0.5f),
                track = track,
                currentTime = currentTime,
                totalTime = totalTime,
                playPauseIcon = playPauseIcon,
                playOrToggleTrack = playOrToggleTrack,
                playPreviousTrack = playPreviousTrack,
                onSliderChange = onSliderChange,
                onRewind = onRewind,
                onForward = onForward,
                playNextTrack = playNextTrack,
                onShowMenu = onShowMenu
            )
        })
    }
}

@Composable
private fun ControllerPart(
    modifier: Modifier = Modifier,
    track: Track,
    currentTime: Long,
    totalTime: Long,
    @DrawableRes playPauseIcon: Int,
    playOrToggleTrack: () -> Unit,
    playPreviousTrack: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onShowMenu: () -> Unit,
    playNextTrack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .aspectRatio(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = track.title, style = MaterialTheme.typography.headlineSmall,
                color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                track.artist,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 0.60f
                    }
                    .basicMarquee()
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = {}
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Rounded.QueueMusic,
                    contentDescription = "Queue Music",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            IconButton(
                onClick = {}
            ) {
                Image(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            IconButton(
                onClick = onShowMenu
            ) {
                Image(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }

        LineMusicSlider(
            modifier = Modifier.fillMaxWidth(),
            currentTime = currentTime,
            totalTime = totalTime,
            onSliderChange = onSliderChange
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
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
                        width = 2.dp, color = Color.White.copy(alpha = 0.8f), shape = CircleShape
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

@Composable
private fun ColumnPortraitRowLandscape(
    modifier: Modifier = Modifier,
    contentRow: @Composable RowScope.() -> Unit,
    contentColumn: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(
                modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                content = contentRow
            )
        }

        else -> {
            Column(modifier) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    content = contentColumn
                )
            }
        }
    }
}


@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun Preview() {
    StUiPreview {
        Box(Modifier.background(Color.Gray)) {
            TrackFullScreenBodyContent(onEvent = {},
                musicControllerUiState = MusicControllerUiState(
                    playerState = PlayerState.PLAYING,
                    currentTrack = Track.getTestTrack(),
                    currentPosition = 20L,
                    totalDuration = 100L,
                ),
                onShowMenu = {},
                onNavigateUp = {})
        }
    }
}

@Preview(
    showSystemUi = true, device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp"
)
@Composable
private fun Preview2() {
    StUiPreview {
        Box(Modifier.background(Color.Gray)) {
            TrackFullScreenBodyContent(onEvent = {},
                musicControllerUiState = MusicControllerUiState(
                    playerState = PlayerState.PLAYING,
                    currentTrack = Track.getTestTrack(),
                    currentPosition = 20L,
                    totalDuration = 100L,
                ),
                onNavigateUp = {},
                onShowMenu = {})
        }
    }
}