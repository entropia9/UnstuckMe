package com.entropia.helpmepick.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.entropia.helpmepick.AppViewModelProvider
import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.AddEditItemsScreen
import com.entropia.helpmepick.ui.AddEditItemsScreenDestination
import com.entropia.helpmepick.ui.MainScreen
import com.entropia.helpmepick.ui.MainScreenDestination
import com.entropia.helpmepick.ui.battlemode.BattleModeDestination
import com.entropia.helpmepick.ui.battlemode.BattleModeScreen
import com.entropia.helpmepick.ui.battlemode.BattleModeSelectScreen
import com.entropia.helpmepick.ui.battlemode.BattleModeSelectScreenDestination
import com.entropia.helpmepick.ui.regularmode.PickRandomScreen
import com.entropia.helpmepick.ui.regularmode.RegularDestination
import com.entropia.helpmepick.ui.regularmode.RegularSelectScreen
import com.entropia.helpmepick.ui.regularmode.RegularSelectScreenDestination
import com.entropia.helpmepick.ui.stats.ExpandedStatsScreen
import com.entropia.helpmepick.ui.stats.StatsDestination
import com.entropia.helpmepick.ui.stats.StatsScreen

@Composable
fun HelpMePickNavHost(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = MainScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = MainScreenDestination.route) {
            MainScreen(
                themeViewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateToAddEditItems = { navController.navigate(AddEditItemsScreenDestination.route) },
                navigateToRegular = { navController.navigate(RegularSelectScreenDestination.route) },
                navigateToBattleMode = { navController.navigate(BattleModeSelectScreenDestination.route) },
                navigateToStats = { navController.navigate(StatsDestination.route) },
                modifier = when (windowSizeClass.windowWidthSizeClass) {
                    WindowWidthSizeClass.EXPANDED -> Modifier.fillMaxWidth(0.5f)
                    else -> Modifier
                }
            )
        }
        composable(route = AddEditItemsScreenDestination.route) {
            AddEditItemsScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigateUp() },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
        composable(route = RegularSelectScreenDestination.route) {
            RegularSelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(RegularDestination.route) },
                navigateToAddEdit = { navController.navigate(AddEditItemsScreenDestination.route) },
                navigateUp = { navController.navigateUp() },
                windowSizeClass = windowSizeClass
            )
        }
        composable(route = BattleModeSelectScreenDestination.route) {
            BattleModeSelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(BattleModeDestination.route) },
                navigateToAddEdit = { navController.navigate(AddEditItemsScreenDestination.route) },
                navigateUp = { navController.navigateUp() },
                windowSizeClass = windowSizeClass
            )
        }
        composable(route = BattleModeDestination.route) {
            BattleModeScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigate(MainScreenDestination.route) },
                modifier = when (windowSizeClass.windowWidthSizeClass) {
                    WindowWidthSizeClass.EXPANDED -> Modifier.padding(100.dp)
                    else -> Modifier
                }
            )
        }
        composable(
            route = RegularDestination.route
        ) {
            PickRandomScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigate(MainScreenDestination.route) },
                navigateToSelect = { navController.navigate(RegularSelectScreenDestination.route) },
                modifier = when (windowSizeClass.windowWidthSizeClass) {
                    WindowWidthSizeClass.EXPANDED -> Modifier.padding(100.dp)
                    else -> Modifier
                }
            )
        }
        composable(
            route = StatsDestination.route
        ) {
            when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.EXPANDED -> {
                    ExpandedStatsScreen(
                        navigateUp = { navController.navigateUp() },
                        viewModel = viewModel(factory = AppViewModelProvider.Factory),
                        navigateToBattleMode = {
                            navController.navigate(
                                BattleModeSelectScreenDestination.route
                            )
                        },
                        navigateToRegularMode = {
                            navController.navigate(
                                RegularSelectScreenDestination.route
                            )
                        },
                        navigateToAddEdit = {
                            navController.navigate(AddEditItemsScreenDestination.route)
                        },
                        modifier = Modifier.padding(50.dp)
                    )
                }

                else -> {
                    StatsScreen(
                        navigateUp = { navController.navigateUp() },
                        viewModel = viewModel(factory = AppViewModelProvider.Factory),
                        navigateToBattleMode = {
                            navController.navigate(
                                BattleModeSelectScreenDestination.route
                            )
                        },
                        navigateToRegularMode = {
                            navController.navigate(
                                RegularSelectScreenDestination.route
                            )
                        },
                        navigateToAddEdit = {
                            navController.navigate(AddEditItemsScreenDestination.route)
                        }
                    )
                }
            }

        }
    }
}