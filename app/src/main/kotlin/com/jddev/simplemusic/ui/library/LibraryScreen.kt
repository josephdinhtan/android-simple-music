package com.jddev.simplemusic.ui.library

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
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
import com.jddev.simpletouch.ui.component.StUiScrollBehavior
import com.jddev.simpletouch.ui.component.StUiTopAppBar
import com.jddev.simpletouch.ui.theme.StUiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
private val musicCategory = mutableListOf(
    LibraryItem("Tracks", content = { _, _ -> Text("Tracks") }),
    LibraryItem("Artists", content = { _, _ -> Text("Artists") }),
    LibraryItem("Albums", content = { _, _ -> Text("Albums") }),
    LibraryItem("Favorites", content = { _, _ -> Text("Favorites") }),
    LibraryItem("Playlists", content = { _, _ -> Text("Playlists") }),
    LibraryItem("Genres", content = { _, _ -> Text("Genres") }),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
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
    musicCategory[0] = LibraryItem("Tracks", content = { scrollBehavior, bottomPadding ->
        SmUiList(modifier = Modifier.fillMaxSize(),
            scrollBehavior = scrollBehavior,
            bottomPadding = bottomPadding,
            smListData = trackGroupsToSmItemList(allTracks),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onTrackSelected(allTracks[index]) })
    })
    musicCategory[1] = LibraryItem("Artists", content = { scrollBehavior, bottomPadding ->
        SmUiList(modifier = Modifier.fillMaxSize(),
            scrollBehavior = scrollBehavior,
            bottomPadding = bottomPadding,
            smListData = artistTrackGroupsToSmItemList(artists),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onArtistGroupSelected(artists[index]) })
    })
    musicCategory[2] = LibraryItem("Albums", content = { scrollBehavior, bottomPadding ->
        SmUiList(modifier = Modifier.fillMaxSize(),
            scrollBehavior = scrollBehavior,
            bottomPadding = bottomPadding,
            smListData = albumTrackGroupsToSmItemList(albums),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onAlbumGroupSelected(albums[index]) })
    })

    val scrollBehavior = StUiScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            StUiTopAppBar(
                modifier = modifier,
                scrollBehavior = scrollBehavior,
                title = "Simple Music",
                actions = {
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
            scrollBehavior = scrollBehavior,
            contents = musicCategory,
            bottomPadding = bottomPadding,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    bottomPadding: Dp,
    contents: List<LibraryItem>,
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
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                contents[page].content(scrollBehavior, bottomPadding)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    StUiTheme {
        LibraryScreen(
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