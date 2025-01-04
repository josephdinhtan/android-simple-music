package com.jddev.simplemusic.ui.library.artist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArtistRoute(
    artistViewModel: ArtistViewModel = hiltViewModel(),
    artistId: Long,
    onBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        artistViewModel.queryTracksGivenArtist(artistId)
    }
    val tracks = artistViewModel.tracks.collectAsState()
    val artist = artistViewModel.artist.collectAsState()
    ArtistScreen(
        artist = artist.value,
        tracks = tracks.value,
        onTrackSelected = { track ->
            artistViewModel.playATrack(track.id)
        },
        getAlbumArt = artistViewModel::getAlbumArt,
        onBack = onBack,
    )
}