package com.jddev.simplemusic.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.utils.listui.SmUiList
import com.jddev.simplemusic.ui.utils.listui.albumTrackGroupsToSmItemList
import com.jddev.simplemusic.ui.utils.listui.artistTrackGroupsToSmItemList
import com.jddev.simplemusic.ui.utils.listui.trackGroupsToSmItemList
import com.jddev.simpletouch.ui.component.StUiTopAppBar
import com.jddev.simpletouch.ui.theme.StUiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    bottomPadding: Dp = 0.dp,
    allTracks: List<Track>,
    artists: List<Artist>,
    albums: List<Album>,
    getAlbumArt: (albumId: Long?, artistId: Long) -> Bitmap?,
    onTrackSelected: (Track) -> Unit,
    onArtistGroupSelected: (Artist) -> Unit,
    onAlbumGroupSelected: (Album) -> Unit,
    navigateToSettings: () -> Unit,
    requestScanDevice: () -> Unit,
) {
    musicCategory[0] = HomeFragmentItem("Tracks", content = {
        SmUiList(modifier = Modifier.fillMaxSize(),
            smListData = trackGroupsToSmItemList(allTracks),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onTrackSelected(allTracks[index]) })
    })
    musicCategory[1] = HomeFragmentItem("Artists", content = {
        SmUiList(modifier = Modifier.fillMaxSize(),
            smListData = artistTrackGroupsToSmItemList(artists),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onArtistGroupSelected(artists[index]) })
    })
    musicCategory[2] = HomeFragmentItem("Albums", content = {
        SmUiList(modifier = Modifier.fillMaxSize(),
            smListData = albumTrackGroupsToSmItemList(albums),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onAlbumGroupSelected(albums[index]) })
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
            contents = musicCategory,
            bottomPadding = bottomPadding,
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    bottomPadding: Dp,
    contents: List<HomeFragmentItem>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        val firstPagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
            contents.size
        }
        val secondPagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
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
                            firstPagerState.currentPage, firstPagerState.currentPageOffsetFraction
                        )
                    }
                } else if (secondPagerState.isScrollInProgress) {
                    coroutineScope.launch {
                        firstPagerState.scrollToPage(
                            secondPagerState.currentPage, secondPagerState.currentPageOffsetFraction
                        )
                    }
                }
            }
        }
        val fling = PagerDefaults.flingBehavior(
            state = firstPagerState, pagerSnapDistance = PagerSnapDistance.atMost(7)
        )
        HorizontalPager(
            state = firstPagerState,
            pageSize = PageSize.Fixed(120.dp),
            contentPadding = PaddingValues(horizontal = 150.dp),
            flingBehavior = fling,
            snapPosition = SnapPosition.Start
        ) { page ->
            val pageOffset = firstPagerState.getOffsetDistanceInPages(page).absoluteValue
            var fontSize = (20.sp * (1 - pageOffset))
            if (fontSize < 12.sp) fontSize = 12.sp
            TextButton(onClick = {
                coroutineScope.launch {
                    firstPagerState.animateScrollToPage(page)
                }
            }) {
                Text(
                    text = contents[page].title, style = TextStyle(fontSize = fontSize)
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = secondPagerState,
            pageSize = PageSize.Fill,
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = bottomPadding),
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
            artists = listOf(),
            onArtistGroupSelected = {},
            albums = listOf(),
            onAlbumGroupSelected = {},
            getAlbumArt = { _, _ -> null },
        )
    }
}