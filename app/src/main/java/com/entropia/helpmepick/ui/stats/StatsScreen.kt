package com.entropia.helpmepick.ui.stats

import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
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
    BattleWins,
    Completed,
    RegularWinsByCategory,
    BattleWinsByCategory
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
    navigateToAddEdit: () -> Unit,
    viewModel: StatsViewModel,
) {
    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            title = stringResource(id = StatsDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { innerPadding ->
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val maxBarWidth = screenWidth * 0.4f
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            item {
                StatsBox(label = stringResource(id = R.string.regular), content = {
                    DisplayStats(
                        map = viewModel.statsUiState.mostPicked.associate { it.name to it.timesPicked },
                        label = stringResource(id = R.string.most_picked),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.Picked,
                        navigateTo = navigateToRegularMode,
                        maxBarWidth = maxBarWidth
                    )
                    DisplayStats(
                        map = viewModel.statsUiState.mostSelected.associate { it.name to it.timesSelected },
                        label = stringResource(id = R.string.most_selected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.Selected,
                        navigateTo = navigateToRegularMode,
                        maxBarWidth = maxBarWidth
                    )

                    DisplayStats(
                        map = viewModel.statsUiState.mostRejected.associate { it.name to it.timesRejected },
                        label = stringResource(id = R.string.most_rejected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.Rejected,
                        navigateTo = navigateToRegularMode,
                        maxBarWidth = maxBarWidth
                    )
                })
            }
            item {
                StatsBox(content = {
                    DisplayStats(
                        map = viewModel.statsUiState.mostBattleWins.associate { it.name to it.battleWins },
                        label = stringResource(id = R.string.most_battle_wins),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.BattleWins,
                        navigateTo = navigateToBattleMode,
                        maxBarWidth = maxBarWidth
                    )
                    DisplayStats(
                        map = viewModel.statsUiState.mostSelectedBattleMode.associate { it.name to it.timesSelectedBattleMode },
                        label = stringResource(id = R.string.most_selected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.SelectedBattleMode,
                        navigateTo = navigateToBattleMode,
                        maxBarWidth = maxBarWidth
                    )
                    DisplayStats(
                        map = viewModel.statsUiState.mostRejectedBattleMode.associate { it.name to it.timesRejectedBattleMode },
                        label = stringResource(id = R.string.most_rejected),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.RejectedBattleMode,
                        navigateTo = navigateToBattleMode,
                        maxBarWidth = maxBarWidth
                    )
                }, label = stringResource(id = R.string.battle_mode))

            }
            item {
                StatsBox(content = {
                    DisplayStats(
                        map = viewModel.statsUiState.completed,
                        label = stringResource(id = R.string.most_completed),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_complete),
                        statsType = StatsType.Completed,
                        navigateTo =navigateToAddEdit,
                        maxBarWidth = maxBarWidth
                    )
                    DisplayStats(
                        map = viewModel.statsUiState.regularWinsByCategory,
                        label = stringResource(id = R.string.most_picked),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                        statsType = StatsType.RegularWinsByCategory,
                        navigateTo = navigateToRegularMode,
                        maxBarWidth = maxBarWidth
                    )
                    DisplayStats(
                        map = viewModel.statsUiState.battleWinsByCategory,
                        label = stringResource(id = R.string.most_battle_wins),
                        noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                        statsType = StatsType.BattleWinsByCategory,
                        navigateTo = navigateToBattleMode,
                        maxBarWidth = maxBarWidth
                    )
                }, label = stringResource(id = R.string.by_categories))

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedStatsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToBattleMode: () -> Unit,
    navigateToRegularMode: () -> Unit,
    navigateToAddEdit: () -> Unit,
    viewModel: StatsViewModel,
) {
    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            title = stringResource(id = StatsDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { innerPadding ->
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val mainBarWidth = screenWidth * 0.5f
        val smallBarWidth = screenWidth * 0.15f
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            item {
                StatsBox(label = stringResource(id = R.string.regular), content = {
                    Column() {
                        DisplayStats(
                            map = viewModel.statsUiState.mostPicked.associate { it.name to it.timesPicked },
                            label = stringResource(id = R.string.most_picked),
                            noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                            statsType = StatsType.Picked,
                            navigateTo = navigateToRegularMode,
                            maxBarWidth = mainBarWidth
                        )
                        Row() {

                            DisplayStats(
                                map = viewModel.statsUiState.mostSelected.associate { it.name to it.timesSelected },
                                label = stringResource(id = R.string.most_selected),
                                noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                                statsType = StatsType.Selected,
                                navigateTo = navigateToRegularMode,
                                modifier = Modifier.weight(0.5f),
                                maxBarWidth = smallBarWidth
                            )

                            DisplayStats(
                                map = viewModel.statsUiState.mostRejected.associate { it.name to it.timesRejected },
                                label = stringResource(id = R.string.most_rejected),
                                noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                                statsType = StatsType.Rejected,
                                navigateTo = navigateToRegularMode,
                                modifier = Modifier.weight(0.5f),
                                maxBarWidth = smallBarWidth
                            )
                        }
                    }


                })
            }
            item {
                StatsBox(content = {
                    Column {
                        DisplayStats(
                            map = viewModel.statsUiState.mostBattleWins.associate { it.name to it.battleWins },
                            label = stringResource(id = R.string.most_battle_wins),
                            noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                            statsType = StatsType.BattleWins,
                            navigateTo = navigateToBattleMode,
                            maxBarWidth = mainBarWidth
                        )
                        Row {
                            DisplayStats(
                                map = viewModel.statsUiState.mostSelectedBattleMode.associate { it.name to it.timesSelectedBattleMode },
                                label = stringResource(id = R.string.most_selected),
                                noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                                statsType = StatsType.SelectedBattleMode,
                                navigateTo = navigateToBattleMode,
                                modifier = Modifier.weight(0.3f),
                                maxBarWidth = smallBarWidth
                            )
                            DisplayStats(
                                map = viewModel.statsUiState.mostRejectedBattleMode.associate { it.name to it.timesRejectedBattleMode },
                                label = stringResource(id = R.string.most_rejected),
                                noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                                statsType = StatsType.RejectedBattleMode,
                                navigateTo = navigateToBattleMode,
                                modifier = Modifier.weight(0.3f),
                                maxBarWidth = smallBarWidth
                            )
                        }
                    }

                }, label = stringResource(id = R.string.battle_mode))
            }

            item {
                StatsBox(content = {
                    Column {
                        DisplayStats(
                            map = viewModel.statsUiState.completed,
                            label = stringResource(id = R.string.most_completed),
                            noItemsButtonLabel = stringResource(id = R.string.no_items_button_complete),
                            statsType = StatsType.Completed,
                            navigateTo = navigateToAddEdit,
                            maxBarWidth = mainBarWidth
                        )
                        Row {
                            DisplayStats(
                                map = viewModel.statsUiState.regularWinsByCategory,
                                label = stringResource(id = R.string.most_picked),
                                noItemsButtonLabel = stringResource(id = R.string.no_items_button_regular_mode),
                                statsType = StatsType.RegularWinsByCategory,
                                navigateTo = navigateToRegularMode,
                                modifier = Modifier.weight(0.3f),
                                maxBarWidth = smallBarWidth
                            )
                            DisplayStats(
                                map = viewModel.statsUiState.battleWinsByCategory,
                                label = stringResource(id = R.string.most_battle_wins),
                                noItemsButtonLabel = stringResource(id = R.string.no_items_button_battle_mode),
                                statsType = StatsType.BattleWinsByCategory,
                                navigateTo = navigateToBattleMode,
                                modifier = Modifier.weight(0.3f),
                                maxBarWidth = smallBarWidth
                            )
                        }
                    }

                }, label = stringResource(id = R.string.by_categories))
            }
        }
    }
}


@Composable
fun StatsBox(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
    label: String,
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
    map: Map<String, Int>,
    label: String,
    noItemsButtonLabel: String,
    statsType: StatsType,
    navigateTo: () -> Unit,
    modifier: Modifier = Modifier,
    maxBarWidth: Dp = 150.dp,
) {
    if (map.isNotEmpty()) {
        MostByStats(
            map = map,
            label = label,
            statsType = statsType,
            modifier = modifier,
            maxBarWidth = maxBarWidth
        )
    } else {
        EmptyListCard(
            text = stringResource(id = R.string.no_items), buttonText = noItemsButtonLabel,
            modifier = modifier
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
    navigateTo: () -> Unit,
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
    map: Map<String, Int>,
    statsType: StatsType,
    modifier: Modifier = Modifier,
    maxBarWidth: Dp = 150.dp,
) {
    Log.d("map",label + map.keys.toString() + map.values.toString())
    val inputList: List<BarData> = map.map { record ->
        BarData(
            value = record.value,
            label = record.key,
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
                        ),
                    maxBarWidth = maxBarWidth
                )
                if (map.size > numberOfItemsVisibleInStats) {
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
    StatsType.Completed -> MaterialTheme.colorScheme.primaryContainer
    StatsType.RegularWinsByCategory -> MaterialTheme.colorScheme.secondaryContainer
    StatsType.BattleWinsByCategory -> MaterialTheme.colorScheme.tertiaryContainer
}
