package com.jddev.simplemusic.ui.home.album

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AlbumRoute(
    albumViewModel: AlbumViewModel = hiltViewModel(),
    albumId: Long,
    onBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        albumViewModel.queryTracksGivenAlbum(albumId)
    }
    val tracks = albumViewModel.tracks.collectAsState()
    val album = albumViewModel.album.collectAsState()
    AlbumScreen(
        album = album.value,
        tracks = tracks.value,
        onTrackSelected = { track ->
            albumViewModel.playATrack(track.id)
        },
        onBack = onBack,
    )
}