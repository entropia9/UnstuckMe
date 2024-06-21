package com.entropia.helpmepick.ui.battlemode

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import com.entropia.helpmepick.ui.theme.Shapes
import kotlinx.coroutines.delay

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
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {

                    val rightButtonState = remember {
                        MutableTransitionState(false).apply {
                            // Start the animation immediately.
                            targetState = true
                        }
                    }
                    val leftButtonState = remember {
                        MutableTransitionState(false).apply {
                            // Start the animation immediately.
                            targetState = true
                        }
                    }
                    var trigger by remember { mutableStateOf(false) }

                    LaunchedEffect(trigger) {
                        rightButtonState.apply { targetState = false }
                        leftButtonState.apply { targetState = false }
                        delay(200)
                        rightButtonState.apply { targetState = true }
                        leftButtonState.apply { targetState = true }
                        trigger = false
                    }
                    AnimatedVisibility(
                        visibleState = rightButtonState,
                        enter = slideInHorizontally { width -> -width / 3 },
                        exit = slideOutHorizontally { 3 * it / 2 }
                    ) {
                        Row() {
                            Box(
                                modifier = modifier
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.onPrimary,
                                                MaterialTheme.colorScheme.secondaryContainer
                                            ),
                                        ),
                                        shape = Shapes.extraLarge
                                    )
                                    .padding(dimensionResource(id = R.dimen.padding_large))
                            ) {
                                Button(onClick = {
                                    trigger = true
                                    viewModel.battleModeUiState.item1?.let {
                                        viewModel.onItemPick(
                                            it
                                        )
                                    }
                                }) {
                                    viewModel.battleModeUiState.item1?.let {
                                        Text(
                                            text = it.name,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    AnimatedVisibility(
                        visibleState = leftButtonState,
                        enter = slideInHorizontally { width -> width / 3 },
                        exit = slideOutHorizontally { -3 * it / 2 }
                    ) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = modifier
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.secondaryContainer,
                                                MaterialTheme.colorScheme.onPrimary
                                            ),
                                        ),
                                        shape = Shapes.extraLarge
                                    )
                                    .padding(dimensionResource(id = R.dimen.padding_large))
                            )
                            {
                                Button(onClick = {
                                    trigger = true
                                    viewModel.battleModeUiState.item2?.let {
                                        viewModel.onItemPick(
                                            it
                                        )
                                    }
                                }) {
                                    viewModel.battleModeUiState.item2?.let {
                                        Text(
                                            text = it.name,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                            }
                        }
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