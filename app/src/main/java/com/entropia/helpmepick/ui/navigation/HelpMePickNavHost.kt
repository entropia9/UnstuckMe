package com.entropia.helpmepick.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.entropia.helpmepick.AppViewModelProvider
import com.entropia.helpmepick.ui.BattleModeDestination
import com.entropia.helpmepick.ui.BattleModeScreen
import com.entropia.helpmepick.ui.BattleModeSelectScreen
import com.entropia.helpmepick.ui.BattleModeSelectScreenDestination
import com.entropia.helpmepick.ui.MainScreen
import com.entropia.helpmepick.ui.MainScreenDestination
import com.entropia.helpmepick.ui.PickRandomScreen
import com.entropia.helpmepick.ui.RegularDestination
import com.entropia.helpmepick.ui.RegularSelectScreen
import com.entropia.helpmepick.ui.RegularSelectScreenDestination
import com.entropia.helpmepick.ui.StatsDestination
import com.entropia.helpmepick.ui.StatsScreen

@Composable
fun HelpMePickNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = MainScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = MainScreenDestination.route) {
            MainScreen(
                navigateToRegular = { navController.navigate(RegularSelectScreenDestination.route) },
                navigateToBattleMode = { navController.navigate(BattleModeSelectScreenDestination.route) },
                navigateToStats = { navController.navigate(StatsDestination.route) })
        }
        composable(route = RegularSelectScreenDestination.route) {
            RegularSelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(RegularDestination.route) },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = BattleModeSelectScreenDestination.route) {
            BattleModeSelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(BattleModeDestination.route) },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = BattleModeDestination.route) {
            BattleModeScreen(viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigateUp() })
        }
        composable(
            route = RegularDestination.route
        ) {
            PickRandomScreen(viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigateUp() })
        }
        composable(
            route = StatsDestination.route
        ) {
            StatsScreen(
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }
    }
}