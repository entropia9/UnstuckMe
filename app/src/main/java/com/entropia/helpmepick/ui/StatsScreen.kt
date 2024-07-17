package com.entropia.helpmepick.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.barchart.BarChart
import com.entropia.helpmepick.ui.barchart.BarData
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import java.lang.Integer.min


enum class StatsType {
    Selected,
    SelectedBattleMode,
    Picked,
    Rejected,
    RejectedBattleMode,
    BattleWins
}

object StatsDestination : NavigationDestination {
    override val route: String
        get() = "stats_screen"
    override val titleRes: Int
        get() = R.string.stats

}

const val numberOfItemsVisibleInStats = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToBattleMode: () -> Unit,
    navigateToRegularMode: () -> Unit,
    viewModel: StatsViewModel
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = stringResource(id = StatsDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                StatsBox(label = stringResource(id = R.string.regular), content = {
                    DisplayStats(
                        list = viewModel.statsUiState.mostPicked,
                        label = stringResource(id = R.string.most_picked),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.Picked,
                        navigateTo = navigateToRegularMode
                    )
                    DisplayStats(
                        list = viewModel.statsUiState.mostSelected,
                        label = stringResource(id = R.string.most_selected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.Selected,
                        navigateTo = navigateToRegularMode
                    )

                    DisplayStats(
                        list = viewModel.statsUiState.mostRejected,
                        label = stringResource(id = R.string.most_rejected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.Rejected,
                        navigateTo = navigateToRegularMode
                    )
                })
            }
            item {
                StatsBox(content = {
                    DisplayStats(
                        list = viewModel.statsUiState.mostBattleWins,
                        label = stringResource(id = R.string.most_battle_wins),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.BattleWins,
                        navigateTo = navigateToBattleMode
                    )
                    DisplayStats(
                        list = viewModel.statsUiState.mostSelectedBattleMode,
                        label = stringResource(id = R.string.most_selected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.SelectedBattleMode,
                        navigateTo = navigateToBattleMode
                    )
                    DisplayStats(
                        list = viewModel.statsUiState.mostRejectedBattleMode,
                        label = stringResource(id = R.string.most_rejected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.RejectedBattleMode,
                        navigateTo = navigateToBattleMode
                    )
                }, label = stringResource(id = R.string.battle_mode))
            }
        }
    }
}

@Composable
fun StatsBox(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
    label: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onPrimary
                    ),
                ),
                shape = RoundedCornerShape(10)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.headlineMedium
            )
            content()
        }

    }
}

@Composable
private fun DisplayStats(
    list: List<Item>,
    label: String,
    noItemsButtonLabel: String,
    statsType: StatsType,
    navigateTo: () -> Unit
) {
    if (list.isNotEmpty()) {
        MostByStats(
            itemList = list,
            label = label,
            statsType = statsType
        )
    } else {
        EmptyListCard(
            text = stringResource(id = R.string.no_items), buttonText = noItemsButtonLabel
        ) {
            navigateTo()
        }
    }
}

@Composable
fun EmptyListCard(
    text: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    navigateTo: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            Button(
                onClick = navigateTo,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text(text = buttonText)
            }
        }
    }
}


@Composable
fun MostByStats(
    label: String,
    itemList: List<Item>,
    statsType: StatsType,
    modifier: Modifier = Modifier
) {

    val inputList: List<BarData> = itemList.map { item ->
        BarData(
            value = determineBarDataValue(statsType, item),
            label = item.name,
            color = determineBarDataColor(statsType)
        )

    }
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label)
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(10)
                    )
            ) {
                BarChart(
                    inputList = if (expanded) inputList else inputList.subList(
                        0,
                        min(numberOfItemsVisibleInStats, inputList.size)
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            dimensionResource(id = R.dimen.padding_medium)
                        )
                )
                if (itemList.size > numberOfItemsVisibleInStats) {
                    ExpandButton(expanded) { expanded = !expanded }
                }
            }
        }
    }

}

@Composable
private fun ExpandButton(expanded: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = if (expanded) stringResource(id = R.string.hide) else stringResource(
                id = R.string.see_all
            )
        )
        Icon(painterResource(id = if (expanded) R.drawable.arrow_up else R.drawable.arrow_down),
            contentDescription = "expand",
            modifier = Modifier
                .padding(
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
                .clickable { onClick() }
        )
    }
}

@Composable
private fun determineBarDataColor(statsType: StatsType) = when (statsType) {
    StatsType.Selected -> MaterialTheme.colorScheme.secondaryContainer
    StatsType.SelectedBattleMode -> MaterialTheme.colorScheme.secondaryContainer
    StatsType.Picked -> MaterialTheme.colorScheme.tertiaryContainer
    StatsType.Rejected -> MaterialTheme.colorScheme.error
    StatsType.RejectedBattleMode -> MaterialTheme.colorScheme.error
    StatsType.BattleWins -> MaterialTheme.colorScheme.primaryContainer
}


private fun determineBarDataValue(
    statsType: StatsType,
    item: Item
) = when (statsType) {
    StatsType.Selected -> item.timesSelected
    StatsType.SelectedBattleMode -> item.timesSelectedBattleMode
    StatsType.Picked -> item.timesPicked
    StatsType.Rejected -> item.timesRejected
    StatsType.RejectedBattleMode -> item.timesRejectedBattleMode
    StatsType.BattleWins -> item.battleWins
}
