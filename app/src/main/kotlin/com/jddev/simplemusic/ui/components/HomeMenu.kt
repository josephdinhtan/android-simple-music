package com.jddev.simplemusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun HomeMenu(
    modifier: Modifier = Modifier, track: Track?, navigateToSettings: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            val imageBitmap = track?.thumbnailBitmap
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
                    track?.title ?: "No Track",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (track != null) {
                    Text(track.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.graphicsLayer {
                            alpha = 0.60f
                        })
                }
            }
        }
        HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        MenuItem(Icons.Default.FavoriteBorder, "Add to favorites")
        MenuItem(Icons.Outlined.AddCircleOutline, "Add to playlist")
        MenuItem(Icons.Outlined.Timer, "Sleep timer")
        MenuItem(Icons.Outlined.Settings, "Settings", onClick = navigateToSettings)
    }
}

@Composable
private fun MenuItem(
    imageVector: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = onClick?.let {
        modifier.clickable { it() }
    } ?: modifier) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector, title, modifier = Modifier.padding(start = 8.dp)
                )
            },
            headlineContent = { Text(title) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    StUiPreview {
        HomeMenu(
            track = Track("track Id", "track title", "track subtitle", ""),
            navigateToSettings = {}
        )
    }
}