package com.entropia.helpmepick.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.entropia.helpmepick.AppViewModelProvider
import com.entropia.helpmepick.ui.BattleModeDestination
import com.entropia.helpmepick.ui.PickRandomScreen
import com.entropia.helpmepick.ui.RegularDestination
import com.entropia.helpmepick.ui.SelectScreen
import com.entropia.helpmepick.ui.SelectScreenDestination

@Composable
fun HelpMePickNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = SelectScreenDestination.route) {
        composable(route = SelectScreenDestination.route) {
            SelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateToRegular = { navController.navigate(RegularDestination.route) },
                navigateToBattleMode = { })
        }
        composable(route = BattleModeDestination.route) {

        }
        composable(
            route = RegularDestination.route
        ) {
            PickRandomScreen(viewModel = viewModel(factory = AppViewModelProvider.Factory))
        }
    }
}