package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.navigation.NavigationDestination

object BattleModeDestination : NavigationDestination {
    override val route: String
        get() = "battle_mode"
    override val titleRes: Int
        get() = R.string.battle_mode

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleModeScreen(
    viewModel: BattleModeViewModel,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {
    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = BattleModeDestination.titleRes),
                canNavigateBack = false, //TODO
                navigateUp = navigateUp
            )
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
        }
    }
}