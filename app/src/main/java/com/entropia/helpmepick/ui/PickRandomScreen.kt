package com.entropia.helpmepick.ui


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.custom.DialogueText
import com.entropia.helpmepick.ui.navigation.NavigationDestination


object RegularDestination : NavigationDestination {
    override val route: String
        get() = "regular_mode"
    override val titleRes: Int
        get() = R.string.regular_mode

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickRandomScreen(
    viewModel: PickRandomViewModel,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = RegularDestination.titleRes),
                canNavigateBack = viewModel.pickItemUiState.currentDialogue == startAgainDialogue || viewModel.pickItemUiState.currentDialogue == pickedDialogue || viewModel.pickItemUiState.currentDialogue == outOfOptions,
                navigateUp = navigateUp
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var firstRun by remember {
                mutableStateOf(true)
            }
            val itemName = viewModel.pickItemUiState.currentPick?.name ?: " "
            Text(text = itemName)
            if (firstRun) {
                Button(onClick = {
                    viewModel.updateSelected(viewModel.pickItemUiState.selectedList)
                    viewModel.pickRandomFromSelected(viewModel.pickItemUiState.selectedList)
                    firstRun = false
                }) {
                    Text(text = stringResource(id = R.string.help_me_pick_btn))
                }
            } else {
                DialogueBar(
                    onYesButtonPressed = {
                        viewModel.updatePickedStatsAndShowDialogue(navigateUp)
                    },
                    onNoButtonPressed = {
                        viewModel.updateRejectedStatsAndShowDialogue(navigateUp)
                    },
                    buttonsVisible = viewModel.pickItemUiState.currentDialogue != pickedDialogue && viewModel.pickItemUiState.currentDialogue != outOfOptionsAgree,
                    text = stringResource(
                        id = viewModel.pickItemUiState.currentDialogue,
                        itemName
                    )
                )
            }

        }
    }
}


@Composable
fun RandomScreen(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Box {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = name)
            }
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxSize()
            ) {

            }


        }

    }

}


@Composable
fun DialogueBar(
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    buttonsVisible: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DialogueText(
            text = text,
            spec = tween(
                durationMillis = text.length * 50,
                easing = LinearEasing
            ),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        if (buttonsVisible) {
            Row {
                Button(onClick = { onYesButtonPressed() }) {
                    Text(text = stringResource(id = R.string.yes_answer_button))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { onNoButtonPressed() }) {
                    Text(text = stringResource(id = R.string.no_answer_button))
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    Column {
        var text = stringResource(id = R.string.question_dialogue1)
        val text2 = stringResource(id = R.string.question_dialogue2)
        RandomScreen("Catfood Calculator")
        DialogueBar(text = text,
            onYesButtonPressed = { text = text2 },
            onNoButtonPressed = {})
    }

}
