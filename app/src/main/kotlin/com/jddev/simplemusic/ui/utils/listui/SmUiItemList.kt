package com.jddev.simplemusic.ui.utils.listui

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.ui.components.ThumbnailImage

@Composable
fun SmItemList(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    thumbnail: Bitmap? = null,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = onClick?.let {
        modifier.clickable { it() }
    } ?: modifier) {
        ListItem(
            leadingContent = {
                ThumbnailImage(
                    modifier = Modifier.size(64.dp),
                    imageBitmap = thumbnail
                )
            },
            headlineContent = { Text(title, style = TextStyle(fontWeight = FontWeight.Bold),maxLines = 1) },
            supportingContent = { Text(subtitle, maxLines = 1) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}