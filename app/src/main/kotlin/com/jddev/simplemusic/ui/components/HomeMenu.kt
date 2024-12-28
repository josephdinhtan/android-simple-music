package com.jddev.simplemusic.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.ui.utils.getTestTrack
import com.jddev.simplemusic.ui.utils.listui.SmItemList
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun HomeMenu(
    track: Track?,
    getAlbumArt: (Long?, Long) -> Bitmap?,
    modifier: Modifier = Modifier,
    onShowBottomSheetTrackInfo: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val imageBitmap = track?.let { getAlbumArt(it.albumId, it.artistId) }

    Column(modifier = modifier) {
        SmItemList(
            title = track?.title ?: "No Track",
            subtitle = track?.artist ?: "Unknown",
            thumbnail = imageBitmap,
        )
        HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        MenuItem(Icons.Default.FavoriteBorder, "Add to favorites")
        MenuItem(Icons.Outlined.AddCircleOutline, "Add to playlist")
        MenuItem(Icons.Outlined.Timer, "Sleep timer")
        MenuItem(Icons.Outlined.Info, "Track details", onClick = onShowBottomSheetTrackInfo)
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
        HomeMenu(track = Track.getTestTrack(),
            navigateToSettings = {},
            onShowBottomSheetTrackInfo = {},
            getAlbumArt = { _, _ -> null })
    }
}