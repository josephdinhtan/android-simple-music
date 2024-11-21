package com.jddev.simplemusic.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmBottomSheet(
    onDismissRequest: () -> Unit,
    content: (@Composable () -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        sheetState = sheetState,
//        shape = RectangleShape,
//        dragHandle = null,
        onDismissRequest = onDismissRequest
    ) {
        content?.invoke()
    }
}