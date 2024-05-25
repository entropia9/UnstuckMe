package com.entropia.helpmepick.ui.custom

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.theme.HelpMePickTheme
import kotlin.math.roundToInt


enum class DragAnchors(val fraction: Float) { Start(0f), End(1f) }

private object Thumb {
    val Size = 60.dp
}

@Composable

fun Thumb(iconRes: Int, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(Thumb.Size)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(35.dp)

        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Track(
    textRes: Int,
    modifier: Modifier = Modifier,
    onDragComplete: () -> Unit,
    thumb: @Composable (BoxScope.() -> Unit)
) {

    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>()
    val state = rememberSaveable(
        saver = AnchoredDraggableState.Saver(animationSpec, positionalThreshold, velocityThreshold)
    ) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec,
        ).apply {
            updateAnchors(DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.End at 1f
            })
        }
    }
    val contentSize = 60.dp
    val contentSizePx = with(density) { contentSize.toPx() }
    val startX = Offset(state.progress * 1000, 0f)
    LaunchedEffect(state.currentValue) {
        if (state.currentValue == DragAnchors.End) onDragComplete()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    topEnd = 20.dp, topStart = 40.dp, bottomEnd = 20.dp, bottomStart = 40.dp
                ),
            ), contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
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
                })
            }

            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    ), Offset.Zero, startX
                ), shape = MaterialTheme.shapes.extraLarge
            )
            .fillMaxWidth()) {
            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(textRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.End
                )
            }
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

}


@Preview
@Composable
fun ButtonPreview() {
    HelpMePickTheme {
        Track(textRes = R.string.regular_mode, onDragComplete = {}) {
            Thumb(iconRes = R.drawable.regular_mode_icon)
        }
    }
}