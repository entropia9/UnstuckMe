package com.entropia.helpmepick.ui.custom

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import kotlin.math.roundToInt


enum class DragAnchors(val fraction: Float) { Start(0f), End(1f) }

private object Thumb {
    val Size = 60.dp
}

@Composable

fun Thumb(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(Thumb.Size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Track(
    modifier: Modifier = Modifier,
    initialValue: DragAnchors = DragAnchors.Start,
    contentSize: Dp = 60.dp,
    onDragComplete: () -> Unit,
    onDragReverse: () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.extraLarge,
    thumb: @Composable (BoxScope.() -> Unit),
    trackContent: @Composable (BoxScope.() -> Unit),
) {

    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>() as AnimationSpec<Float>
    val decayAnimationSpec = splineBasedDecay<Float>(density)
    val state = rememberSaveable(
        saver = AnchoredDraggableState.Saver(
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = decayAnimationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold
        )
    ) {
        AnchoredDraggableState(
            decayAnimationSpec = decayAnimationSpec,
            snapAnimationSpec = animationSpec,
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        ).apply {
            updateAnchors(DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.End at 1f
            }, initialValue)
        }

    }
    val contentSizePx = with(density) { contentSize.toPx() }
    val startX = Offset(state.progress(state.settledValue, state.targetValue) * 1000, 0f)
    LaunchedEffect(state.currentValue) {
        if (state.currentValue == DragAnchors.End && initialValue != DragAnchors.End) onDragComplete()
        if (state.currentValue == DragAnchors.Start && initialValue != DragAnchors.Start) onDragReverse()
    }

    Box(modifier = modifier
        .padding(
            dimensionResource(id = R.dimen.padding_large)
        )
        .onSizeChanged { layoutSize ->

            val dragEndPoint = layoutSize.width - contentSizePx
            state.updateAnchors(DraggableAnchors {
                DragAnchors
                    .values()
                    .forEach { anchor ->
                        anchor at (dragEndPoint * anchor.fraction)
                    }
            }, initialValue)
        }

        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer
                ), Offset.Zero, startX
            ), shape = shape
        )
        .fillMaxWidth()) {
        trackContent()
        Box(modifier = Modifier
            .size(contentSize)
            .offset {
                IntOffset(
                    x = state
                        .requireOffset()
                        .roundToInt(),
                    y = 0,
                )
            }
            .anchoredDraggable(state, Orientation.Horizontal)) {
            thumb()
        }

    }
}