package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.barchart.BarChart
import com.entropia.helpmepick.ui.barchart.BarData
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
            item {
                MostByStats(
                    itemList = viewModel.statsUiState.mostBattleWins,
                    statsType = StatsType.BattleWins
                )
            }

        }
    }
}

@Composable
fun MostByStats(itemList: List<Item>, statsType: StatsType, modifier: Modifier = Modifier) {
    if (itemList.isNotEmpty()) {
        val label = stringResource(
            id = when (statsType) {
                StatsType.Selected -> R.string.most_selected
                StatsType.Picked -> R.string.most_picked
                StatsType.Rejected -> R.string.most_rejected
                StatsType.BattleWins -> R.string.most_battle_wins
            }
        )
        val inputList: List<BarData> = itemList.map { item ->
            BarData(
                value = when (statsType) {
                    StatsType.Selected -> item.timesSelected
                    StatsType.Picked -> item.timesPicked
                    StatsType.Rejected -> item.timesRejected
                    StatsType.BattleWins -> item.battleWins
                },
                label = item.name,
                color = MaterialTheme.colorScheme.primaryContainer
            )

        }

        Column(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label)
            BarChart(inputList = inputList, modifier = Modifier.fillMaxWidth())
        }

    }
}
