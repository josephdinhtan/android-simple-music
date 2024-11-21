package com.jddev.simplemusic.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenBottomSheet(
    onDismissRequest: () -> Unit,
    content: (@Composable () -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        shape = RectangleShape,
        sheetMaxWidth = Dp.Unspecified,
        dragHandle = null,
        onDismissRequest = onDismissRequest
    ) {
        content?.invoke()
    }
}