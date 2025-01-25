package com.entropia.helpmepick.ui.battlemode

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.ExpandedSelectScreen
import com.entropia.helpmepick.ui.addedititem.ItemsListViewModel
import com.entropia.helpmepick.ui.SelectBottomAppBar
import com.entropia.helpmepick.ui.SelectScreen
import com.entropia.helpmepick.ui.navigation.NavigationDestination

object BattleModeSelectScreenDestination : NavigationDestination {
    override val route: String
        get() = "battle_mode_select_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleModeSelectScreen(
    viewModel: ItemsListViewModel,
    navigate: () -> Unit,
    navigateToAddEdit: () -> Unit,
    navigateUp: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    Scaffold(topBar = {
        TopAppBar(
            title = stringResource(id = R.string.select_btn), canNavigateBack = true,
            navigateUp = navigateUp
        )
    },
        bottomBar = {
            SelectBottomAppBar(
                viewModel = viewModel,
                navigate = { navigate() },
                navigateToAddEdit = { navigateToAddEdit() },
                isEnabled = { size -> size >= 4 && size % 4 == 0 }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets) { innerPadding ->
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.EXPANDED -> ExpandedSelectScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )

            else -> SelectScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

}