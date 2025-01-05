package com.jddev.simplemusic.ui.library

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class)
private val musicCategories = mutableListOf(
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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
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
    val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
        musicCategories.size
    }
    musicCategories[0] = LibraryItem("Tracks", content = { scrollBehavior, bottomPadding ->
        SmUiList(modifier = Modifier.fillMaxSize(),
            scrollBehavior = scrollBehavior,
            bottomPadding = bottomPadding,
            smListData = trackGroupsToSmItemList(allTracks),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onTrackSelected(allTracks[index]) })
    })
    musicCategories[1] = LibraryItem("Artists", content = { scrollBehavior, bottomPadding ->
        SmUiList(modifier = Modifier.fillMaxSize(),
            scrollBehavior = scrollBehavior,
            bottomPadding = bottomPadding,
            smListData = artistTrackGroupsToSmItemList(artists),
            getAlbumArt = getAlbumArt,
            onItemIndexSelected = { index -> onArtistGroupSelected(artists[index]) })
    })
    musicCategories[2] = LibraryItem("Albums", content = { scrollBehavior, bottomPadding ->
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
            Column {
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
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier
                ) {
                    musicCategories.forEachIndexed { index, item ->
                        LeadingIconTab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            icon = { Icon(Icons.Outlined.MusicNote, "") },
                            text = { Text(text = item.title) }
                        )
                    }
                }
            }
        },

        ) { innerPadding ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = pagerState,
            pageSize = PageSize.Fill,
        ) { pageIndex ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                musicCategories[pageIndex].content(scrollBehavior, bottomPadding)
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