package com.entropia.helpmepick.ui.rafflemode

import FinalText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.custom.DialogueText
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import com.entropia.helpmepick.ui.pickedDialogue
import com.entropia.helpmepick.ui.regularmode.RandomScreen
import com.entropia.helpmepick.ui.regularmode.RegularDestination
import com.entropia.helpmepick.ui.theme.Shapes


object RaffleModeDestination : NavigationDestination {
    override val route: String
        get() = "raffle_mode"
    override val titleRes: Int
        get() = R.string.raffle_mode

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaffleModeScreen(
    viewModel: RaffleModeViewModel,
    navigateUp: () -> Unit,
    navigateToSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lastDialogueReached =
        viewModel.raffleUiState.currentDialogue == pickedDialogue
    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = RegularDestination.titleRes),
                canNavigateBack = lastDialogueReached,
                navigateUp = navigateUp
            )
        }) { innerPadding ->
        var firstRun by remember {
            mutableStateOf(true)
        }
        val itemName = viewModel.raffleUiState.currentPick?.name ?: " "

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (firstRun) {
                Button(onClick = {
                    viewModel.updateSelected(viewModel.raffleUiState.selectedList)
                    viewModel.pickRandomFromSelected()
                    firstRun = false
                }) {
                    Text(text = stringResource(id = R.string.help_me_pick_btn))
                }
            } else {

                RandomScreen(
                    name = itemName,
                    dialogueText = stringResource(
                        id = viewModel.raffleUiState.currentDialogue,
                        itemName
                    ),
                    onYesButtonPressed = {
                        viewModel.updateStatsAndShowDialogue(
                            navigateToSelect, navigateUp
                        )
                    },
                    onNoButtonPressed = {

                        viewModel.nextDialogue()
                    },
                    lastDialogueReached = lastDialogueReached,
                    modifier = Modifier
                        .fillMaxSize(),
                    listSize = 1
                )
            }
        }
        AnimatedVisibility(
            visible = viewModel.raffleUiState.currentDialogue == pickedDialogue,
            enter = slideInVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_large)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.1f))
                FinalText(
                    text = stringResource(
                        id = R.string.win_dialogue,
                        itemName
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.padding_large)
                    )
                )
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.colorScheme.onPrimary
                                ),
                            ),
                            shape = Shapes.large
                        )
                        .padding(dimensionResource(id = R.dimen.padding_large))
                ) {

                    Column {
                        val stringBuilder = StringBuilder()
                        viewModel.raffleUiState.pickedItem?.let { item ->
                            stringBuilder.append(
                                pluralStringResource(
                                    id = R.plurals.picked_stats1_raffle_mode,
                                    count = item.timesSelectedRaffle, item.timesSelectedRaffle
                                )
                            ).appendLine().appendLine().append(
                                when (item.timesRejectedRaffle) {
                                    0 -> stringResource(id = R.string.zero)
                                    else -> pluralStringResource(
                                        id = R.plurals.picked_stats2,
                                        count = item.timesRejectedRaffle,
                                        item.timesRejectedRaffle
                                    )
                                }
                            ).appendLine().appendLine().append(
                                pluralStringResource(
                                    id = R.plurals.picked_stats3,
                                    count = item.raffleWins,
                                    item.raffleWins
                                )
                            ).toString()
                                .let { DialogueText(text = it) }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = { navigateUp() }, modifier = Modifier.padding(
                        dimensionResource(
                            id = R.dimen.padding_medium
                        )
                    )
                ) {
                    Text(text = stringResource(id = R.string.proceed))
                }
                Spacer(modifier = Modifier.weight(0.02f))
            }
        }
    }
}

