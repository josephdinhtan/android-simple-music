package com.jddev.simplemusic.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.components.ThumbnailImage
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun AllTracksContent(
    modifier: Modifier = Modifier,
    tracks: List<Track>,
    onTrackSelected: (Track) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(tracks) {
            TrackItemList(track = it, onClick = { onTrackSelected(it) })
        }
    }
}

@Composable
private fun TrackItemList(
    modifier: Modifier = Modifier, track: Track, onClick: (() -> Unit)? = null
) {
    Box(modifier = onClick?.let {
        modifier.clickable { it() }
    } ?: modifier) {
        ListItem(
            leadingContent = {
                if (track.thumbnailBitmap != null) {
                    ThumbnailImage(
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                        imageBitmap = track.thumbnailBitmap!!
                    )
                } else {
                    ThumbnailImage(modifier = Modifier.padding(start = 16.dp, end = 8.dp))
                }
            },
            headlineContent = { Text(track.title, maxLines = 1) },
            supportingContent = { Text(track.subtitle, maxLines = 1) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    val track = Track("test", "test", "test", "test")
    StUiPreview {
        AllTracksContent(tracks = listOf(track, track, track), onTrackSelected = {})
    }
}