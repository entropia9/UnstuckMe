package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
        Column(modifier = Modifier.padding(innerPadding)) {
            MostByStats(
                itemList = viewModel.statsUiState.mostSelected,
                label = stringResource(id = R.string.most_selected)
            )
        }
    }
}

@Composable
fun MostByStats(itemList: List<Item>, label: String, modifier: Modifier = Modifier) {
    if (itemList.isNotEmpty()) {
        val nameList=itemList.map { item -> item.name  }
        val maxRange= itemList[0].timesSelected

        val barData:List<BarData> = itemList.mapIndexed { index, item->
            BarData(
                point = Point(item.timesSelected.toFloat(), index.toFloat()),
                label = item.name,
                dataCategoryOptions = DataCategoryOptions(isDataCategoryInYAxis = true)
            )
        }
        val xStepSize = 1

        val xAxisData = AxisData.Builder()
            .steps(xStepSize)
            .bottomPadding(12.dp)
            .endPadding(40.dp)
            .labelData { index -> (index * (maxRange / xStepSize)).toString() }
            .build()
        val yAxisData = AxisData.Builder()
            .axisStepSize(30.dp)
            .steps(barData.size - 1)
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
            Text(text=label)
            BarChart(
                modifier = Modifier.height(350.dp),
                barChartData = barChartData
            )
        }

    }
}
