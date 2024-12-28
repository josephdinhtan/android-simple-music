package com.jddev.simplemusic.ui.player

import android.graphics.Bitmap
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
import com.jddev.simplemusic.ui.utils.getTestTrack
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun FullPlayerScreen(
    getAlbumArt: (Long?, Long) -> Bitmap?,
    onEvent: (TrackEvent) -> Unit,
    musicControllerUiState: MusicControllerUiState,
    onShowMenu: () -> Unit,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 1) { 3 }

    Box(Modifier.fillMaxSize()) {
        BlurBackgroundImage(
            Modifier.fillMaxSize(),
            imageBitmap = getAlbumArt(
                musicControllerUiState.currentTrack.albumId,
                musicControllerUiState.currentTrack.artistId
            )
        )
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageSize = PageSize.Fill,
        ) { page ->
            when (page) {
                0 -> FullPlayerBodyContent(
                    musicControllerUiState = musicControllerUiState,
                    getAlbumArt = getAlbumArt,
                    onShowMenu = onShowMenu,
                    onNavigateUp = onBack,
                    onEvent = onEvent
                )

                //TODO: Keep same pages, later need to handle other pages
                else -> FullPlayerBodyContent(
                    musicControllerUiState = musicControllerUiState,
                    getAlbumArt = getAlbumArt,
                    onShowMenu = onShowMenu,
                    onNavigateUp = onBack,
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
        FullPlayerScreen(
            onEvent = {},
            musicControllerUiState = MusicControllerUiState(
                playerState = PlayerState.PLAYING,
                currentTrack = Track.getTestTrack(),
                currentPosition = 20L,
                totalDuration = 100L,
            ),
            onBack = {}, onShowMenu = {},
            getAlbumArt = { _, _ -> null },
        )
    }
}

@Preview(
    showSystemUi = true, device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp"
)
@Composable
private fun Preview2() {
    StUiPreview {
        FullPlayerScreen(
            onEvent = {},
            musicControllerUiState = MusicControllerUiState(
                playerState = PlayerState.PLAYING,
                currentTrack = Track.getTestTrack(),
                currentPosition = 20L,
                totalDuration = 100L,
            ),
            onBack = {}, onShowMenu = {},
            getAlbumArt = { _, _ -> null },
        )
    }
}