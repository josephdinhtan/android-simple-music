package com.jddev.simplemusic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineMusicSlider(
    modifier: Modifier = Modifier,
    currentTime: Long,
    totalTime: Long,
    onSliderChange: (Float) -> Unit
) {
//    val sliderColors = if (isSystemInDarkTheme()) {
//        SliderDefaults.colors(
//            thumbColor = MaterialTheme.colorScheme.onBackground,
//            activeTrackColor = MaterialTheme.colorScheme.onBackground,
//            inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(
//                alpha = 0.8f
//            ),
//        )
//    } else SliderDefaults.colors(
//        thumbColor = MaterialTheme.colorScheme.onBackground,
//        activeTrackColor = MaterialTheme.colorScheme.onBackground,
//        inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(
//            alpha = 0.8f
//        ),
//    )
    val sliderColors = SliderDefaults.colors(
        thumbColor = Color.White,
        activeTrackColor = Color.White,
        inactiveTrackColor = Color.White.copy(
            alpha = 0.8f
        ),
    )
    val thumbSize = 16.dp
    Slider(
        value = max(currentTime.toFloat(), 0f),
        valueRange = 0f..max(totalTime.toFloat(), 0f),
        colors = sliderColors,
        onValueChange = onSliderChange,
        thumb = {
            Box(
                Modifier
                    .size(thumbSize)
                    .background(sliderColors.thumbColor, CircleShape)
            )
        },
        track = { sliderState ->
            val fraction =
                (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
            Box(Modifier.fillMaxWidth()) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .align(Alignment.CenterStart)
                        .height(6.dp)
                        .padding(end = thumbSize / 2)
                        .background(sliderColors.activeTrackColor, CircleShape)
                )
                Box(
                    Modifier
                        .fillMaxWidth(1f - fraction)
                        .align(Alignment.CenterEnd)
                        .height(1.dp)
                        .padding(start = thumbSize / 2)
                        .background(sliderColors.inactiveTrackColor, CircleShape)
                )
            }
        }
    )
}

@Preview()
@Composable
private fun Preview() {
    LineMusicSlider(
        currentTime = 30L,
        totalTime = 100L,
        onSliderChange = {}
    )
}