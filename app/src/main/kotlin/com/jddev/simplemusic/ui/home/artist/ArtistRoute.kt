package com.jddev.simplemusic.ui.home.artist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.home.HomeViewModel

@Composable
fun ArtistRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    artistId: Long,
    onTrackSelected: (Track) -> Unit,
    onBack: () -> Unit,
) {
    val artists = homeViewModel.artists.collectAsState()
    ArtistScreen(
        artistTrackGroup = ArtistTrackGroup("Test", null, emptyList()),//artists.value.first{ it.artist == artistId },
        onTrackSelected = onTrackSelected,
        onBack = onBack,
    )
}