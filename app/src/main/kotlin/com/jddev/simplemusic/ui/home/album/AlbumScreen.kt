package com.jddev.simplemusic.ui.home.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.utils.listui.SmList
import com.jddev.simplemusic.ui.utils.listui.trackGroupsToSmItemList
import com.jddev.simpletouch.ui.component.StUiTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    albumTrackGroup: AlbumTrackGroup,
    onTrackSelected: (Track) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            StUiTopAppBar(
                modifier = modifier,
                title = albumTrackGroup.album,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            SmList(
                modifier = Modifier.fillMaxSize(),
                smListData = trackGroupsToSmItemList(albumTrackGroup.tracks),
                onItemIndexSelected = { index -> onTrackSelected(albumTrackGroup.tracks[index]) }
            )
        }
    }
}