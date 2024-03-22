package com.entropia.helpmepick.ui.custom

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


private val defaultWidth = 16.dp


enum class FadingSide {
    LEFT, RIGHT
}


fun Size.getFadeOffset(side: FadingSide): Pair<Offset, Offset> {
    return when (side) {
        FadingSide.LEFT -> Offset.Zero to Offset(width, 0f)
        FadingSide.RIGHT -> Offset(width, 0f) to Offset.Zero
    }
}

fun Modifier.fadingEdge(
    vararg sides: FadingSide,
    color: Color,
    width: Dp = defaultWidth,
    isVisible: Boolean = true,
    spec: AnimationSpec<Dp>? = null
) = composed {
    require(width > 0.dp) { "Invalid fade width: Width must be greater than 0" }

    val animatedWidth = spec?.let {
        animateDpAsState(
            targetValue = if (isVisible) width else 0.dp,
            animationSpec = spec,
            label = "Fade width"
        ).value
    }

    drawWithContent {
        this@drawWithContent.drawContent()

        sides.forEach { side ->
            val (start, end) = this.size.getFadeOffset(side)

            val staticWidth = if (isVisible) width.toPx() else 0f
            val widthPx = animatedWidth?.toPx() ?: staticWidth

            val fraction = widthPx / this.size.width

            drawRect(
                brush = Brush.linearGradient(
                    0f to color,
                    fraction to Color.Transparent,
                    start = start,
                    end = end
                ),
                size = this.size
            )
        }
    }
}