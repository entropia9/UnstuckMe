package com.entropia.helpmepick.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.navigation.NavigationDestination


object RegularDestination : NavigationDestination {
    override val route: String
        get() = "regular_mode"
    override val titleRes: Int
        get() = R.string.regular_mode

}


@Composable
fun PickRandomScreen(
    viewModel: PickRandomViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var firstRun by remember {
            mutableStateOf(true)
        }
        Text(text = viewModel.pickItemUiState.currentPick?.name ?: " ")
        if (firstRun) {
            Button(onClick = {
                viewModel.pickRandomFromSelected(viewModel.pickItemUiState.selectedList)
                viewModel.updateSelected(viewModel.pickItemUiState.selectedList)
                firstRun = false
            }) {
                Text(text = stringResource(id = R.string.help_me_pick_btn))
            }
        } else {
            DialogueBar(
                onYesButtonPressed = { viewModel.updatePicked() },
                onNoButtonPressed = {
                    viewModel.updateRejected()
                    viewModel.pickRandomFromSelected(viewModel.pickItemUiState.selectedList)
                },
                text = stringResource(id = viewModel.pickItemUiState.currentDialogue)
            )
        }

    }

}


@Composable
fun DialogueBar(
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
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

@Preview
@Composable
fun Preview() {
    DialogueBar(text = stringResource(id = R.string.question_dialogue1),
        onYesButtonPressed = {},
        onNoButtonPressed = {})
}
