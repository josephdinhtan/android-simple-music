package com.jddev.simplemusic.ui.track

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.MusicControllerUiState
import com.jddev.simplemusic.ui.components.BlurBackgroundImage
import com.jddev.simplemusic.ui.components.TrackEvent
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun TrackScreen(
    onEvent: (TrackEvent) -> Unit,
    musicControllerUiState: MusicControllerUiState,
    onShowMenu: () -> Unit,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 1) { 3 }

    Box(Modifier.fillMaxSize()) {
        BlurBackgroundImage(
            Modifier.fillMaxSize(),
            musicControllerUiState.currentTrack.thumbnailBitmap
        )
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageSize = PageSize.Fill,
        ) { page ->
            when (page) {
                0 -> TrackFullScreenBodyContent(
                    onShowMenu = onShowMenu,
                    onNavigateUp = onBack,
                    musicControllerUiState = musicControllerUiState,
                    onEvent = onEvent
                )

                else -> TrackFullScreenBodyContent(
                    onShowMenu = onShowMenu,
                    onNavigateUp = onBack,
                    musicControllerUiState = musicControllerUiState,
                    onEvent = onEvent
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
        TrackScreen(onEvent = {}, musicControllerUiState = MusicControllerUiState(
            playerState = PlayerState.PLAYING,
            currentTrack = Track("Test Track", "Test Track", "Test single", ""),
            currentPosition = 20L,
            totalDuration = 100L,
        ), onBack = {}, onShowMenu = {})
    }
}

@Preview(
    showSystemUi = true, device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp"
)
@Composable
private fun Preview2() {
    StUiPreview {
        TrackScreen(onEvent = {}, musicControllerUiState = MusicControllerUiState(
            playerState = PlayerState.PLAYING,
            currentTrack = Track("Test Track", "Test Track", "Test single", ""),
            currentPosition = 20L,
            totalDuration = 100L,
        ), onBack = {}, onShowMenu = {})
    }
}