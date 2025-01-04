package com.jddev.simplemusic.ui.library

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

data class LibraryItem @OptIn(ExperimentalMaterial3Api::class) constructor(
    val title: String,
    val content: @Composable (scrollBehavior: TopAppBarScrollBehavior?, bottomPadding: Dp) -> Unit,
)
