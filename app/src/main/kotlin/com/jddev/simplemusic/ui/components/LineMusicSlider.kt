package com.jddev.simplemusic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jddev.simplemusic.updatest.toTime
import timber.log.Timber
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineMusicSlider(
    modifier: Modifier = Modifier,
    currentTime: Long,
    totalTime: Long,
    onSliderChange: (Float) -> Unit
) {
    val sliderColors = SliderDefaults.colors(
        thumbColor = Color.White,
        activeTrackColor = Color.White,
        inactiveTrackColor = Color.White.copy(
            alpha = 0.6f
        ),
    )
    val thumbSize = 16.dp
    var sliderValue by remember { mutableFloatStateOf(max(currentTime.toFloat(), 0f)) }
    var userChangingValue by remember { mutableStateOf(false) }

    Timber.e("Joseph userChangingValue: $userChangingValue , sliderValue: $sliderValue")

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Slider(
            value = if (userChangingValue) sliderValue else max(currentTime.toFloat(), 0f),
            valueRange = 0f..max(totalTime.toFloat(), 0f),
            colors = sliderColors,
            onValueChange = {
                sliderValue = it
                userChangingValue = true
            },
            onValueChangeFinished = {
                onSliderChange(sliderValue)
                userChangingValue = false
            },
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
                            //.padding(end = thumbSize / 2)
                            .background(sliderColors.activeTrackColor, CircleShape)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth(1f - fraction)
                            .align(Alignment.CenterEnd)
                            .height(1.dp)
                            //.padding(start = thumbSize / 2)
                            .background(sliderColors.inactiveTrackColor, CircleShape)
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = (if (userChangingValue) sliderValue else currentTime).toLong().toTime(),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
//                                }
//                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                totalTime.toTime(),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
//                                }
        }
    }

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