package com.jddev.simplemusic.ui.home

import androidx.compose.runtime.Composable

data class HomeFragmentItem(
    val title: String,
    val content: @Composable () -> Unit,
)
