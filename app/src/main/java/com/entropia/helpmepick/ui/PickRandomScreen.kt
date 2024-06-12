package com.entropia.helpmepick.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.custom.DialogueText
import com.entropia.helpmepick.ui.custom.ShakeConfig
import com.entropia.helpmepick.ui.custom.rememberShakeController
import com.entropia.helpmepick.ui.custom.shake
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import com.entropia.helpmepick.ui.theme.Shapes
import kotlinx.coroutines.delay


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
        var firstRun by remember {
            mutableStateOf(true)
        }
        val itemName = viewModel.pickItemUiState.currentPick?.name ?: " "

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (firstRun) {
                Button(onClick = {
                    viewModel.updateSelected(viewModel.pickItemUiState.selectedList)
                    viewModel.pickRandomFromSelected(viewModel.pickItemUiState.selectedList)
                    firstRun = false
                }) {
                    Text(text = stringResource(id = R.string.help_me_pick_btn))
                }
            } else {
                RandomScreen(
                    name = itemName,
                    dialogueText = stringResource(
                        id = viewModel.pickItemUiState.currentDialogue,
                        itemName
                    ),
                    onYesButtonPressed = { viewModel.updatePickedStatsAndShowDialogue(navigateUp) },
                    onNoButtonPressed = { viewModel.updateRejectedStatsAndShowDialogue(navigateUp) },
                    lastDialogueReached = viewModel.pickItemUiState.currentDialogue == pickedDialogue || viewModel.pickItemUiState.currentDialogue == outOfOptionsAgree,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_large))
                )
            }

        }
    }
}


@Composable
fun RandomScreen(
    name: String, modifier: Modifier = Modifier,
    dialogueText: String,
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit,
    lastDialogueReached: Boolean
) {
    val shakeController = rememberShakeController()
    var visible by remember { mutableStateOf(false) }
    val iterations = 10
    LaunchedEffect(visible) {
        if (visible) {
            delay(200)
            shakeController.shake(
                ShakeConfig(
                    iterations = iterations,
                    intensity = 1_000f,
                    rotateY = (-15..15).random().toFloat(),
                    rotateX = (-15..15).random().toFloat(),
                    translateX = (-40..40).random().toFloat(),
                    translateY = (-40..40).random().toFloat()
                )
            )
            delay((iterations * 400).toLong())
            visible = false
            delay(200)
        }
    }
    AnimatedVisibility(
        visible = !lastDialogueReached,
        exit = fadeOut()
    ) {
        Column(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .shake(shakeController)
                    .padding(30.dp)
            ) {
                Box {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = name)
                    }
                    this@Column.AnimatedVisibility(
                        visible = visible,
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ) {
                        Box(
                            Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .fillMaxSize()
                        ) {

                        }
                    }

                }

            }
            DialogueBar(
                onYesButtonPressed = {
                    onYesButtonPressed()

                },
                onNoButtonPressed = {
                    visible = true
                    onNoButtonPressed()
                },
                text = dialogueText,
                textVisible = !visible
            )
        }
    }
    AnimatedVisibility(visible = lastDialogueReached, enter = fadeIn()) {
        DialogueText(text = dialogueText)
    }
}


@Composable
fun DialogueBar(
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textVisible: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onPrimary
                    ),
                ),
                shape = Shapes.extraLarge
            ),
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (textVisible) {
                DialogueText(
                    text = text,
                    spec = tween(
                        durationMillis = text.length * 50,
                        easing = LinearEasing
                    ),
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
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
}

@Preview
@Composable
fun Preview() {

    Column {
        var text = stringResource(id = R.string.question_dialogue1)
        val text2 = stringResource(id = R.string.question_dialogue2)
        RandomScreen(
            "Catfood Calculator", dialogueText = text,
            onYesButtonPressed = { text = text2 },
            onNoButtonPressed = {},
            lastDialogueReached = true
        )
//            DialogueBar(text = text,
//                onYesButtonPressed = { text = text2 },
//                onNoButtonPressed = {})
    }


}
