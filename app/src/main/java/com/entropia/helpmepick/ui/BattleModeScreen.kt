package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    viewModel: BattleModeViewModel, modifier: Modifier = Modifier, navigateUp: () -> Unit
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = stringResource(id = BattleModeDestination.titleRes),
            canNavigateBack = viewModel.battleModeUiState.winner != null,
            navigateUp = navigateUp
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (viewModel.battleModeUiState.winner == null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        viewModel.battleModeUiState.item1?.let {
                            viewModel.onItemPick(
                                it
                            )
                        }
                    }) {
                        viewModel.battleModeUiState.item1?.let { Text(text = it.name) }
                    }
                    Button(onClick = {
                        viewModel.battleModeUiState.item2?.let {
                            viewModel.onItemPick(
                                it
                            )
                        }
                    }) {
                        viewModel.battleModeUiState.item2?.let { Text(text = it.name) }
                    }
                }
            } else {
                Text(
                    text = stringResource(
                        id = R.string.win_dialogue,
                        viewModel.battleModeUiState.winner!!.name
                    )
                )
            }
        }
    }
}