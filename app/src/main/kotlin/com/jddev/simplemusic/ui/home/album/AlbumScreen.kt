package com.jddev.simplemusic.ui.home.album

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.utils.listui.SmUiList
import com.jddev.simplemusic.ui.utils.listui.trackGroupsToSmItemList
import com.jddev.simpletouch.ui.component.StUiCircularProgressIndicator
import com.jddev.simpletouch.ui.component.StUiTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    album: Album?,
    tracks: List<Track>,
    onTrackSelected: (Track) -> Unit,
    getAlbumArt: (albumId: Long?, artistId: Long) -> Bitmap?,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            StUiTopAppBar(
                modifier = modifier,
                title = album?.name ?: "Unknown",
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            album?.let {
                SmUiList(modifier = Modifier.fillMaxSize(),
                    smListData = trackGroupsToSmItemList(tracks),
                    getAlbumArt = getAlbumArt,
                    onItemIndexSelected = { index ->
                        onTrackSelected(tracks[index])
                    })
            } ?: StUiCircularProgressIndicator()
        }
    }
}