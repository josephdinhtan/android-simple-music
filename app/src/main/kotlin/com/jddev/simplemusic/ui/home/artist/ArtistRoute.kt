package com.jddev.simplemusic.ui.home.artist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.home.HomeViewModel

@Composable
fun ArtistRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    artist: String,
    onTrackSelected: (Track) -> Unit,
    onBack: () -> Unit,
) {
    val artistTracks = homeViewModel.artistTracks.collectAsState()
    ArtistScreen(
        artistTrackGroup = artistTracks.value.first{ it.artist == artist },
        onTrackSelected = onTrackSelected,
        onBack = onBack,
    )
}