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
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.components.TrackBottomBar
import com.jddev.simplemusic.ui.components.TrackEvent
import com.jddev.simpletouch.ui.component.StUiTopAppBar
import com.jddev.simpletouch.ui.theme.StUiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.absoluteValue

private val musicCategory = mutableListOf(
    HomeFragmentItem("Tracks", content = { Text("Tracks") }),
    HomeFragmentItem("Favorites", content = { Text("Favorites") }),
    HomeFragmentItem("Playlists", content = { Text("Playlists") }),
    HomeFragmentItem("Artists", content = { Text("Artists") }),
    HomeFragmentItem("Albums", content = { Text("Albums") }),
    HomeFragmentItem("Genres", content = { Text("Genres") }),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentTrack: Track? = null,
    playerState: PlayerState,
    allTracks: List<Track>,
    onTrackEvent: (TrackEvent) -> Unit,
    navigateToSettings: () -> Unit,
    onTrackSelected: (Track) -> Unit,
    onShowTrackFullScreen: () -> Unit,
    requestScanDevice: () -> Unit,
) {
    musicCategory[0] = HomeFragmentItem("Tracks", content = {
        AllTracksContent(
            modifier = Modifier.fillMaxSize(),
            tracks = allTracks,
            onTrackSelected = onTrackSelected,
        )
    })
    Scaffold(
        modifier = modifier,
        topBar = {
            StUiTopAppBar(
                modifier = modifier,
                title = "Simple Music",
                actions = {
                    IconButton(onClick = requestScanDevice) {
                        Icon(Icons.Default.FlipCameraAndroid, "reload")
                    }
                    IconButton(onClick = {}) {
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
            TrackBottomBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onTrackEvent = onTrackEvent,
                playerState = playerState,
                track = currentTrack,
                onBarClick = {
                    onShowTrackFullScreen()
                }
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
            val pageOffset = firstPagerState.getOffsetDistanceInPages(page).absoluteValue
            Timber.d("page $page: pageOffset $pageOffset")
            var fontSize = (20.sp * (1 - pageOffset))
            if(fontSize < 12.sp) fontSize = 12.sp
            TextButton(onClick = {
                coroutineScope.launch {
                    firstPagerState.animateScrollToPage(page)
                }
            }) {
                Text(
                    text = contents[page].title,
                    style = TextStyle(fontSize = fontSize)
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
            playerState = PlayerState.PLAYING,
            currentTrack = Track("id", "title", "subtitle", "trackUrl"),
            allTracks = listOf(),
            onTrackEvent = {},
            navigateToSettings = {},
            onTrackSelected = {},
            onShowTrackFullScreen = {},
            requestScanDevice = {}
        )
    }
}