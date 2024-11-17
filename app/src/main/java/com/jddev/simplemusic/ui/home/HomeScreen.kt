package com.jddev.simplemusic.ui.home

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.components.PlayerState
import com.jddev.simplemusic.ui.components.TrackBottomController
import com.jddev.simpletouch.ui.component.StUiTopAppBar
import com.jddev.simpletouch.ui.theme.StUiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

private val musicCategory =
    listOf("Spotify", "Favorites", "Playlists", "Artists", "Albums", "Genres", "Tracks")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    onShowTrackScreen: (trackId: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        topBar = {
            StUiTopAppBar(
                modifier = modifier,
                title = "Simple Music",
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(Icons.Outlined.Search, "Search")
                    }
                    IconButton(onClick = navigateToSettings) {
                        Icon(Icons.Default.MoreVert, "More options")
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeContent(coroutineScope)
            TrackBottomController(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
                onEvent = {},
                playerState = PlayerState.PLAYING,
                track = Track(
                    title = "Title",
                    subtitle = "Subtitle",
                    imageUrl = "",
                    songUrl = "",
                    mediaId = "0"
                ),
                onBarClick = onShowTrackScreen
            )
        }
    }
}

@Composable
private fun HomeContent(coroutineScope: CoroutineScope) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val firstPagerState =
            rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
                musicCategory.size
            }
        val secondPagerState =
            rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
                musicCategory.size
            }
        LaunchedEffect(firstPagerState) {
            snapshotFlow { firstPagerState.currentPage }.collect { page ->
                coroutineScope.launch {
                    firstPagerState.animateScrollToPage(page)
                }
            }
        }
        LaunchedEffect(
            key1 = firstPagerState.currentPageOffsetFraction,
            key2 = secondPagerState.currentPageOffsetFraction
        ) {
            if (firstPagerState.currentPageOffsetFraction != secondPagerState.currentPageOffsetFraction) {
                if (firstPagerState.isScrollInProgress) {
                    coroutineScope.launch {
                        secondPagerState.scrollToPage(
                            firstPagerState.currentPage,
                            firstPagerState.currentPageOffsetFraction
                        )
                    }
                } else if (secondPagerState.isScrollInProgress) {
                    coroutineScope.launch {
                        firstPagerState.scrollToPage(
                            secondPagerState.currentPage,
                            secondPagerState.currentPageOffsetFraction
                        )
                    }
                }
            }
        }
        val fling = PagerDefaults.flingBehavior(
            state = firstPagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(7)
        )
        HorizontalPager(
            state = firstPagerState, beyondViewportPageCount = 7,
            pageSize = PageSize.Fixed(100.dp),
            contentPadding = PaddingValues(horizontal = 150.dp),
            flingBehavior = fling,
            snapPosition = SnapPosition.Start
        ) { page ->
            // Our page content
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    firstPagerState.animateScrollToPage(page)
                }
            }) {
                Text(
                    text = musicCategory[page],
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        HorizontalPager(
            state = secondPagerState,
            pageSize = PageSize.Fill,
        ) { page ->
            // Our page content
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = musicCategory[page],
                )
            }
        }
        Text("Page selected: ${musicCategory[firstPagerState.currentPage]}")
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    StUiTheme {
        HomeScreen(
            modifier = Modifier,
            {}, {}
        )
    }
}