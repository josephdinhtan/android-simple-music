package com.jddev.simplemusic.ui.home

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
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
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.home.album.AlbumTrackGroup
import com.jddev.simplemusic.ui.home.artist.ArtistTrackGroup
import com.jddev.simplemusic.ui.utils.listui.SmList
import com.jddev.simplemusic.ui.utils.listui.albumTrackGroupsToSmItemList
import com.jddev.simplemusic.ui.utils.listui.artistTrackGroupsToSmItemList
import com.jddev.simplemusic.ui.utils.listui.trackGroupsToSmItemList
import com.jddev.simpletouch.ui.component.StUiTopAppBar
import com.jddev.simpletouch.ui.theme.StUiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.absoluteValue

private val musicCategory = mutableListOf(
    HomeFragmentItem("Tracks", content = { Text("Tracks") }),
    HomeFragmentItem("Artists", content = { Text("Artists") }),
    HomeFragmentItem("Albums", content = { Text("Albums") }),
    HomeFragmentItem("Favorites", content = { Text("Favorites") }),
    HomeFragmentItem("Playlists", content = { Text("Playlists") }),
    HomeFragmentItem("Genres", content = { Text("Genres") }),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    allTracks: List<Track>,
    artistGroups: List<ArtistTrackGroup>,
    albumGroups: List<AlbumTrackGroup>,
    onTrackSelected: (Track) -> Unit,
    onArtistGroupSelected: (ArtistTrackGroup) -> Unit,
    onAlbumGroupSelected: (AlbumTrackGroup) -> Unit,
    navigateToSettings: () -> Unit,
    requestScanDevice: () -> Unit,
) {
    musicCategory[0] = HomeFragmentItem("Tracks", content = {
        SmList(
            modifier = Modifier.fillMaxSize(),
            smListData = trackGroupsToSmItemList(allTracks),
            onItemIndexSelected = { index -> onTrackSelected(allTracks[index]) }
        )
    })
    musicCategory[1] = HomeFragmentItem("Artists", content = {
        SmList(
            modifier = Modifier.fillMaxSize(),
            smListData = artistTrackGroupsToSmItemList(artistGroups),
            onItemIndexSelected = { index -> onArtistGroupSelected(artistGroups[index]) }
        )
    })
    musicCategory[2] = HomeFragmentItem("Albums", content = {
        SmList(
            modifier = Modifier.fillMaxSize(),
            smListData = albumTrackGroupsToSmItemList(albumGroups),
            onItemIndexSelected = { index -> onAlbumGroupSelected(albumGroups[index]) }
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
        HomeContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contents = musicCategory
        )
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
            if (fontSize < 12.sp) fontSize = 12.sp
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
            allTracks = listOf(),
            navigateToSettings = {},
            onTrackSelected = {},
            requestScanDevice = {},
            artistGroups = listOf(),
            onArtistGroupSelected = {},
            albumGroups = listOf(),
            onAlbumGroupSelected = {}
        )
    }
}