package com.entropia.helpmepick.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.entropia.helpmepick.AppViewModelProvider
import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.AddEditItemsScreen
import com.entropia.helpmepick.ui.AddEditItemsScreenDestination
import com.entropia.helpmepick.ui.MainScreen
import com.entropia.helpmepick.ui.MainScreenDestination
import com.entropia.helpmepick.ui.StatsDestination
import com.entropia.helpmepick.ui.StatsScreen
import com.entropia.helpmepick.ui.battlemode.BattleModeDestination
import com.entropia.helpmepick.ui.battlemode.BattleModeScreen
import com.entropia.helpmepick.ui.battlemode.BattleModeSelectScreen
import com.entropia.helpmepick.ui.battlemode.BattleModeSelectScreenDestination
import com.entropia.helpmepick.ui.regularmode.PickRandomScreen
import com.entropia.helpmepick.ui.regularmode.RegularDestination
import com.entropia.helpmepick.ui.regularmode.RegularSelectScreen
import com.entropia.helpmepick.ui.regularmode.RegularSelectScreenDestination

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
                themeViewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateToAddEditItems = { navController.navigate(AddEditItemsScreenDestination.route) },
                navigateToRegular = { navController.navigate(RegularSelectScreenDestination.route) },
                navigateToBattleMode = { navController.navigate(BattleModeSelectScreenDestination.route) },
                navigateToStats = { navController.navigate(StatsDestination.route) })
        }
        composable(route = AddEditItemsScreenDestination.route) {
            AddEditItemsScreen(viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigateUp() },
                modifier=Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
        }
        composable(route = RegularSelectScreenDestination.route) {
            RegularSelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(RegularDestination.route) },
                navigateToAddEdit = { navController.navigate(AddEditItemsScreenDestination.route) },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = BattleModeSelectScreenDestination.route) {
            BattleModeSelectScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigate = { navController.navigate(BattleModeDestination.route) },
                navigateToAddEdit = { navController.navigate(AddEditItemsScreenDestination.route) },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = BattleModeDestination.route) {
            BattleModeScreen(viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigate(MainScreenDestination.route) })
        }
        composable(
            route = RegularDestination.route
        ) {
            PickRandomScreen(viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateUp = { navController.navigate(MainScreenDestination.route) })
        }
        composable(
            route = StatsDestination.route
        ) {
            StatsScreen(
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateToBattleMode = { navController.navigate(BattleModeSelectScreenDestination.route) },
                navigateToRegularMode = { navController.navigate(RegularSelectScreenDestination.route) }
            )
        }
    }
}