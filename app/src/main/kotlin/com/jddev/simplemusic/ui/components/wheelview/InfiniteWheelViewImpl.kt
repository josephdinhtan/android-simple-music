package com.jddev.simplemusic.ui.components.wheelview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun InfiniteWheelViewImpl(
    modifier: Modifier,
    itemSize: DpSize,
    selection: Int,
    itemCount: Int,
    columnOffset: Int,
    isEndless: Boolean,
    onFocusItem: (Int) -> Unit,
    selectorOption: SelectorOptions,
    userScrollEnabled: Boolean = true,
    lazyWheelState: LazyListState? = null,
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val count = if (isEndless) itemCount else itemCount + 2 * columnOffset
    val columnOffsetCount = maxOf(1, minOf(columnOffset, 4))
    val columnCount = ((columnOffsetCount * 2) + 1)
    val startIndex = if (isEndless) selection + (itemCount * 1000) - columnOffset else selection

    val state = lazyWheelState ?: rememberLazyListState(startIndex)

    val size = DpSize(itemSize.width * columnCount, itemSize.height)

    val isScrollInProgress = state.isScrollInProgress
    val focusedIndex = remember {
        derivedStateOf { state.firstVisibleItemIndex + columnOffsetCount }
    }

    LaunchedEffect(key1 = itemCount) {
        coroutineScope.launch {
            state.scrollToItem(startIndex)
        }
    }

    LaunchedEffect(key1 = isScrollInProgress) {
        if (!isScrollInProgress) {
            calculateIndexToFocus(state, size.height).let {
                val indexToFocus = if (isEndless) {
                    (it + columnOffsetCount) % itemCount
                } else {
                    ((it + columnOffsetCount) % count) - columnOffset
                }

                onFocusItem(indexToFocus)
                if (state.firstVisibleItemScrollOffset != 0) {
                    coroutineScope.launch {
                        state.animateScrollToItem(it, 0)
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }.collect {
            if (selectorOption.selectEffectEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }


    Box(
        modifier = modifier
            //.height(size.height)
            .height(100.dp)
            .fillMaxWidth(),
    ) {
        LazyRow (
            modifier = Modifier
//                .height(size.height)
                .height(100.dp)
                .fillMaxWidth(),
            state = state,
            userScrollEnabled = userScrollEnabled,
        ) {
            items(if (isEndless) Int.MAX_VALUE else count) {
                val rotateDegree = calculateIndexRotation(focusedIndex.value, it, columnOffset)
//                Box(
//                    modifier = Modifier
//                        .width(size.width / columnCount)
////                        .fillMaxWidth(),
////                        .graphicsLayer {
////                            this.rotationY = rotateDegree
////                        },
////                    contentAlignment = Alignment.Center,
//                ) {
//                    if (isEndless) {
//                        content(it % itemCount)
//                    } else if (it >= columnOffsetCount && it < itemCount + columnOffsetCount) {
//                        content((it - columnOffsetCount) % itemCount)
//                    }
//                }

                if (isEndless) {
                    content(it % itemCount)
                } else if (it >= columnOffsetCount && it < itemCount + columnOffsetCount) {
                    content((it - columnOffsetCount) % itemCount)
                }
            }
        }


        if (selectorOption.enabled) {
            SelectionView(
                selectorOptions = selectorOption, columnOffset = columnOffset
            )
        }
    }
}


private fun calculateIndexToFocus(listState: LazyListState, height: Dp): Int {
    val currentItem = listState.layoutInfo.visibleItemsInfo.firstOrNull()
    var index = currentItem?.index ?: 0

    if (currentItem?.offset != 0) {
        if (currentItem != null && currentItem.offset <= -height.value * 3 / 10) {
            index++
        }
    }
    return index
}

@Composable
private fun calculateIndexRotation(focusedIndex: Int, index: Int, offset: Int): Float {
    return (6 * offset + 1).toFloat() * (focusedIndex - index)
}