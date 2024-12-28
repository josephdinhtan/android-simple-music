package com.jddev.simplemusic.ui.utils.listui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jddev.simplemusic.ui.utils.simpleVerticalScrollbar
import com.jddev.simplemusic.updatest.StUiPreview

@Composable
fun SmUiList(
    modifier: Modifier = Modifier,
    smListData: List<SmItemData>,
    getAlbumArt: (albumId: Long?, artistId: Long) -> Bitmap?,
    onItemSelected: ((SmItemData) -> Unit)? = null,
    onItemIndexSelected: ((Int) -> Unit)? = null,
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(
            state = listState, modifier = Modifier.fillMaxSize().simpleVerticalScrollbar(listState)
        ) {
            itemsIndexed(smListData) { index, item ->
                val thumbnail = getAlbumArt(item.albumId, item.artistId)
                SmItemList(
                    title = item.title,
                    subtitle = item.subtitle,
                    thumbnail = thumbnail,
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