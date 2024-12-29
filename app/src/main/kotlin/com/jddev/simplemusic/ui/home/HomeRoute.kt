package com.jddev.simplemusic.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.ui.components.HomeMenu
import com.jddev.simplemusic.ui.components.SmBottomSheet

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    bottomPadding: Dp,
    navigateToSettings: () -> Unit,
    onArtistGroupSelected: (Artist) -> Unit,
    onAlbumGroupSelected: (Album) -> Unit,
) {
    val allTracks = homeViewModel.allTracks.collectAsState()
    val currentTrack = homeViewModel.currentTrack.collectAsState()
    val artists = homeViewModel.artists.collectAsState()
    val albums = homeViewModel.albums.collectAsState()

    var showBottomSheetMenu by remember { mutableStateOf(false) }

    Box {
        HomeScreen(
            bottomPadding = bottomPadding,
            allTracks = allTracks.value,
            artists = artists.value,
            albums = albums.value,
            navigateToSettings = {
                showBottomSheetMenu = true
            },
            requestScanDevice = { homeViewModel.scanDevice() },
            onTrackSelected = { track ->
                homeViewModel.playATrack(track.id)
            },
            getAlbumArt = homeViewModel::getAlbumArt,
            onArtistGroupSelected = onArtistGroupSelected,
            onAlbumGroupSelected = onAlbumGroupSelected,
        )
    }
    if (showBottomSheetMenu) {
        SmBottomSheet(
            onDismissRequest = { showBottomSheetMenu = false },
        ) {
            HomeMenu(
                modifier = Modifier.fillMaxWidth(),
                track = currentTrack.value,
                getAlbumArt = homeViewModel::getAlbumArt,
                navigateToSettings = {
                    showBottomSheetMenu = false
                    navigateToSettings()
                },
                onShowBottomSheetTrackInfo = {}
            )
        }
    }
}