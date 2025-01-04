package com.jddev.simplemusic.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.jddev.simplemusic.R

@Composable
fun ThumbnailImage(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    imageBitmap: Bitmap? = null
) {
    imageBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Track title",
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(shape)
        )
    } ?: Image(
        painter = painterResource(id = R.drawable.song_img),
        contentDescription = "Track title",
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(shape)
    )
}