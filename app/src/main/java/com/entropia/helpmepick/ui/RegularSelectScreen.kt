package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
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
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.select_btn),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        SelectScreen(
            viewModel = viewModel,
            navigate = { navigate() },
            isEnabled = { size -> size >= 2 },
            modifier = Modifier.padding(innerPadding)
        )
    }

}