package com.jddev.simplemusic.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.R

@Composable
fun ThumbnailImage(
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap
) {
    Image(
        bitmap = imageBitmap.asImageBitmap(),
        contentDescription = "Track title",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(48.dp)
            .clip(shape = MaterialTheme.shapes.medium)
    )
}

@Composable
fun ThumbnailImage(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.song_img),
        contentDescription = "Track title",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(48.dp)
            .clip(shape = MaterialTheme.shapes.medium)
    )
}