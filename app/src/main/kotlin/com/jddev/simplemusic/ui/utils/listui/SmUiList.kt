package com.jddev.simplemusic.ui.utils.listui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.ui.utils.simpleVerticalScrollbar
import com.jddev.simplemusic.updatest.StUiPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmUiList(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    bottomPadding: Dp = 0.dp,
    smListData: List<SmItemData>,
    getAlbumArt: (albumId: Long?, artistId: Long) -> Bitmap?,
    onItemSelected: ((SmItemData) -> Unit)? = null,
    onItemIndexSelected: ((Int) -> Unit)? = null,
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(state = listState, modifier = scrollBehavior?.nestedScrollConnection?.let {
            Modifier
                .fillMaxSize()
                .nestedScroll(it)
                .simpleVerticalScrollbar(listState)
        } ?: Modifier
            .fillMaxSize()
            .simpleVerticalScrollbar(listState)
        ) {
            itemsIndexed(smListData) { index, item ->
                val thumbnail = getAlbumArt(item.albumId, item.artistId)
                SmItemList(title = item.title,
                    subtitle = item.subtitle,
                    thumbnail = thumbnail,
                    onClick = {
                        onItemSelected?.invoke(item)
                        onItemIndexSelected?.invoke(index)
                    })
            }
            item {
                Spacer(Modifier.height(bottomPadding))
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    StUiPreview {
        SmItemList(title = "Title", subtitle = "Subtitle", onClick = {})
    }
}