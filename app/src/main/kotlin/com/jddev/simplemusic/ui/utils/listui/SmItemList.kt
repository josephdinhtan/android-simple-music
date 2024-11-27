package com.jddev.simplemusic.ui.utils.listui

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.ui.components.ThumbnailImage
import com.jddev.simplemusic.ui.utils.simpleVerticalScrollbar
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun SmList(
    modifier: Modifier = Modifier,
    smListData: List<SmItemData>,
    onItemSelected: ((SmItemData) -> Unit)? = null,
    onItemIndexSelected: ((Int) -> Unit)? = null,
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(
            state = listState, modifier = Modifier.fillMaxSize().simpleVerticalScrollbar(listState)
        ) {
            itemsIndexed(smListData) { index, item ->
                SmItemList(
                    title = item.title,
                    subtitle = item.subtitle,
                    thumbnail = item.thumbnail,
                    onClick = {
                        onItemSelected?.invoke(item)
                        onItemIndexSelected?.invoke(index)
                    }
                )
            }
        }
    }
}

@Composable
private fun SmItemList(
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
                if (thumbnail != null) {
                    ThumbnailImage(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        imageBitmap = thumbnail
                    )
                } else {
                    ThumbnailImage(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                }
            },
            headlineContent = { Text(title, maxLines = 1) },
            supportingContent = { Text(subtitle, maxLines = 1) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    StUiPreview {
        SmItemList(
            title = "Title",
            subtitle = "Subtitle",
            onClick = {}
        )
    }
}