package com.entropia.helpmepick.ui.barchart

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.theme.Shapes
import kotlinx.coroutines.delay

data class BarData(
    val value: Int,
    val label: String,
    val color: Color
)

@Composable
fun BarChart(
    maxBarWidth: Dp = 120.dp,
    height: Dp = 40.dp,
    inputList: List<BarData>,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val highestValue = inputList.maxOfOrNull { it.value }?.toFloat()

        inputList.forEach { input ->

            Bar(
                width = maxBarWidth * input.value.toFloat() / highestValue!!,
                height = height,
                modifier = Modifier,
                primaryColor = MaterialTheme.colorScheme.onSecondaryContainer,
                secondaryColor = input.color,
                label = input.label,
                value = input.value
            )
        }
    }
}

@Composable
fun Bar(
    width: Dp,
    height: Dp,
    modifier: Modifier,
    primaryColor: Color,
    secondaryColor: Color,
    value: Int,
    label: String
) {
    Row(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth()
    ) {
        var trigger by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(true) {
            delay(500 / value.toLong())
            trigger = true

        }
        val widthDp: Dp by animateDpAsState(
            targetValue = if (trigger) width else 10.dp, label =
            "width_anim"
        )
        Box(
            modifier = modifier
                .width(widthDp)
                .height(height)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor,
                            secondaryColor
                        ),
                    ),
                    shape = Shapes.extraLarge
                )
        ) {}
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Text(
                text = label,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = pluralStringResource(id = R.plurals.stats_count, count = value, value),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                style = MaterialTheme.typography.labelSmall
            )
        }

    }

}


@Preview
@Composable
fun BarChartPreview() {
    val list = listOf(
        BarData(4, "Catfood Calculator", MaterialTheme.colorScheme.primaryContainer),
        BarData(2, "Woman and the Raptor", MaterialTheme.colorScheme.primaryContainer),
        BarData(1, "StrayCalculator", MaterialTheme.colorScheme.primaryContainer),
    )

    BarChart(inputList = list, modifier = Modifier)
}
