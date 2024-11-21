package com.jddev.simplemusic.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.R

@Composable
internal fun BlurBackgroundImage(
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap?
) {
    if (imageBitmap != null) Image(
        modifier = modifier.blur(
            radiusX = 150.dp,
            radiusY = 150.dp,
        ),
        bitmap = imageBitmap.asImageBitmap(),
        contentScale = ContentScale.FillBounds,
        contentDescription = "blur Background",
        colorFilter = ColorFilter.tint(
            Color.Black.copy(alpha = 0.4f),
            blendMode = BlendMode.Darken
        )
    )
    else
        Image(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 50.dp),
            painter = painterResource(id = R.drawable.song_img),
            contentScale = ContentScale.FillBounds,
            contentDescription = "blur Background"
        )
}