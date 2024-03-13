package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.navigation.NavigationDestination


enum class StatsType {
    Selected,
    Picked,
    Rejected,
    BattleWins
}

object StatsDestination : NavigationDestination {
    override val route: String
        get() = "stats_screen"
    override val titleRes: Int
        get() = R.string.stats

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(modifier: Modifier = Modifier, navigateUp: () -> Unit, viewModel: StatsViewModel) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = stringResource(id = StatsDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                MostByStats(
                    itemList = viewModel.statsUiState.mostSelected,
                    statsType = StatsType.Selected
                )
            }
            item {
                MostByStats(
                    itemList = viewModel.statsUiState.mostPicked,
                    statsType = StatsType.Picked
                )
            }
            item {
                MostByStats(
                    itemList = viewModel.statsUiState.mostRejected,
                    statsType = StatsType.Rejected
                )
            }

        }
    }
}

@Composable
fun MostByStats(itemList: List<Item>, statsType: StatsType, modifier: Modifier = Modifier) {
    if (itemList.isNotEmpty()) {
        val nameList = itemList.map { item -> item.name }
        val maxRange = when (statsType) {
            StatsType.Selected -> itemList[0].timesSelected
            StatsType.Picked -> itemList[0].timesPicked
            StatsType.Rejected -> itemList[0].timesRejected
            StatsType.BattleWins -> itemList[0].battleWins
        }
        val label = stringResource(
            id = when (statsType) {
                StatsType.Selected -> R.string.most_selected
                StatsType.Picked -> R.string.most_picked
                StatsType.Rejected -> R.string.most_rejected
                StatsType.BattleWins -> R.string.most_battle_wins
            }
        )
        val barData: List<BarData> = itemList.mapIndexed { index, item ->
            BarData(
                point = Point(
                    when (statsType) {
                        StatsType.Selected -> item.timesSelected.toFloat()
                        StatsType.Picked -> item.timesPicked.toFloat()
                        StatsType.Rejected -> item.timesRejected.toFloat()
                        StatsType.BattleWins -> item.battleWins.toFloat()
                    }, index.toFloat()
                ),
                label = item.name,
                dataCategoryOptions = DataCategoryOptions(isDataCategoryInYAxis = true)
            )
        }
        val xStepSize = 1

        val xAxisData = AxisData.Builder()
            .steps(maxRange/xStepSize)
            .bottomPadding(12.dp)
            .endPadding(40.dp)
            .labelData { index -> (index * xStepSize).toString() }
            .build()
        val yAxisData = AxisData.Builder()
            .axisStepSize(30.dp)
            .steps(barData.size)
            .labelAndAxisLinePadding(20.dp)
            .axisOffset(20.dp)
            .setDataCategoryOptions(
                DataCategoryOptions(
                    isDataCategoryInYAxis = true,
                    isDataCategoryStartFromBottom = false
                )
            )
            .startDrawPadding(48.dp)
            .labelData { index -> nameList[index] }
            .build()
        val barChartData = BarChartData(
            chartData = barData,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                isGradientEnabled = false,
                paddingBetweenBars = 20.dp,
                barWidth = 35.dp,
                selectionHighlightData = SelectionHighlightData(
                    highlightBarColor = Color.Red,
                    highlightTextBackgroundColor = Color.Green,
                    popUpLabel = { x, _ -> " Value : $x " },
                    barChartType = BarChartType.HORIZONTAL
                ),
            ),
            showYAxis = true,
            showXAxis = true,
            horizontalExtraSpace = 20.dp,
            barChartType = BarChartType.HORIZONTAL
        )

        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label)
            BarChart(
                modifier = Modifier.height(350.dp),
                barChartData = barChartData
            )
        }

    }
}
