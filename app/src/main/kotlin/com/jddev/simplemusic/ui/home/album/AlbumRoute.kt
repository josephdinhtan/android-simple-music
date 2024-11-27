package com.jddev.simplemusic.ui.home.album

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.home.HomeViewModel

@Composable
fun AlbumRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    album: String,
    onTrackSelected: (Track) -> Unit,
    onBack: () -> Unit,
) {
    val artistTracks = homeViewModel.albumTracks.collectAsState()
    AlbumScreen(
        albumTrackGroup = artistTracks.value.first{ it.album == album },
        onTrackSelected = onTrackSelected,
        onBack = onBack,
    )
}