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
import com.entropia.helpmepick.ui.MainScreen
import com.entropia.helpmepick.ui.MainScreenDestination
import com.entropia.helpmepick.ui.PickRandomScreen
import com.entropia.helpmepick.ui.RegularDestination
import com.entropia.helpmepick.ui.SelectScreen
import com.entropia.helpmepick.ui.SelectScreenDestination
import com.entropia.helpmepick.ui.StatsDestination

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
                navigateToSelect = { navController.navigate(SelectScreenDestination.route) },
                navigateToStats = { navController.navigate(StatsDestination.route) })
        }
        composable(route = SelectScreenDestination.route) {
            SelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(RegularDestination.route) },
                isEnabled = {it -> false}
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
            //TODO
        }
    }
}