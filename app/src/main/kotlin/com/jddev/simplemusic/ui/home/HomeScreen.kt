package com.jddev.simplemusic.ui.home

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.ui.components.TrackBottomController
import com.jddev.simpletouch.ui.component.StUiTopAppBar
import com.jddev.simpletouch.ui.theme.StUiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val musicCategory = listOf(
    HomeFragmentItem("Spotify", content = { Text("Spotify") }),
    HomeFragmentItem("Favorites", content = { Text("Favorites") }),
    HomeFragmentItem("Playlists", content = { Text("Playlists") }),
    HomeFragmentItem("Artists", content = { Text("Artists") }),
    HomeFragmentItem("Albums", content = { Text("Albums") }),
    HomeFragmentItem("Genres", content = { Text("Genres") }),
    HomeFragmentItem("Tracks", content = { Text("Tracks") })
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    onShowTrackScreen: (trackId: String) -> Unit,
) {
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
            HomeContent(
                modifier = Modifier.fillMaxSize(),
                contents = musicCategory
            )
            TrackBottomController(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onEvent = {},
                playerState = PlayerState.PLAYING,
                track = Track(
                    title = "Title",
                    subtitle = "Subtitle",
                    imageUrl = "",
                    trackUrl = "",
                    mediaId = "0"
                ),
                onBarClick = onShowTrackScreen
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    contents: List<HomeFragmentItem>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        val firstPagerState =
            rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
                contents.size
            }
        val secondPagerState =
            rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
                contents.size
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
            state = firstPagerState,
            pageSize = PageSize.Fixed(120.dp),
            contentPadding = PaddingValues(horizontal = 150.dp),
            flingBehavior = fling,
            snapPosition = SnapPosition.Start
        ) { page ->
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    firstPagerState.animateScrollToPage(page)
                }
            }) {
                Text(
                    text = contents[page].title,
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = secondPagerState,
            pageSize = PageSize.Fill,
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Page:")
                contents[page].content()
            }
        }
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