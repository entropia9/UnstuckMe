package com.entropia.helpmepick.ui.regularmode

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.ExpandedSelectScreen
import com.entropia.helpmepick.ui.ItemsListViewModel
import com.entropia.helpmepick.ui.SelectBottomAppBar
import com.entropia.helpmepick.ui.SelectScreen
import com.entropia.helpmepick.ui.navigation.NavigationDestination

object RegularSelectScreenDestination : NavigationDestination {
    override val route: String
        get() = "regular_select_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularSelectScreen(
    viewModel: ItemsListViewModel,
    navigate: () -> Unit,
    navigateToAddEdit: () -> Unit,
    navigateUp: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.select_btn),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        bottomBar = {
            SelectBottomAppBar(
                viewModel = viewModel,
                navigate = { navigate() },
                navigateToAddEdit = { navigateToAddEdit() },
                isEnabled = { size -> size >= 2 }
            )
        }
    ) { innerPadding ->
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