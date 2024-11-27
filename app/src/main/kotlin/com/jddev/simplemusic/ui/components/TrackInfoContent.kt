package com.jddev.simplemusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.utils.getTestTrack
import com.jddev.simplemusic.updatest.StUiPreview


@Composable
fun TrackInfoContent(
    modifier: Modifier = Modifier,
    track: Track
) {
    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            val imageBitmap = track.thumbnailBitmap
            if (imageBitmap != null) ThumbnailImage(
                imageBitmap = imageBitmap, modifier = Modifier.padding(16.dp)
            )
            else ThumbnailImage(modifier = Modifier.padding(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 8.dp),
            ) {
                Text(
                    track.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.graphicsLayer {
                        alpha = 0.60f
                    }
                )
            }
        }
        HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        MenuItem(title = "Album", subTitle = track.album)
        MenuItem(title = "Album artist", subTitle = "Test Album Artist")
        MenuItem(title = "Genre", subTitle = "All the best")
        MenuItem(title = "Track length", subTitle = "All the best")
        MenuItem(title = "Record data", subTitle = "2025")
        MenuItem(title = "Size", subTitle = "10.5M")
    }
}

@Composable
private fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = onClick?.let {
        modifier.clickable { it() }
    } ?: modifier) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = subTitle?.let { { Text(it) } },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    StUiPreview {
        TrackInfoContent(
            track = Track.getTestTrack(),
        )
    }
}