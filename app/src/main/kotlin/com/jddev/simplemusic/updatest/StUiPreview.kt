package com.jddev.simplemusic.updatest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jddev.simpletouch.ui.theme.StUiTheme

@Composable
fun StUiPreview(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    StUiTheme {
        Scaffold {
            Box(modifier = modifier.fillMaxSize().padding(it)) {
                content()
            }
        }
    }
}