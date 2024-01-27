package com.entropia.helpmepick.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item

@Composable
fun PickRandom(items: List<Item>, viewModel: PickRandomViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = viewModel.pickItemUiState.currentPick?.name ?: " ")
        Button(onClick = {
            viewModel.pickRandomFromSelected(items)
        }) {
            Text(text = stringResource(id = R.string.help_me_pick_btn))
        }
    }

}


@Composable
fun DialogueBar(text: String, modifier: Modifier = Modifier) {
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
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.yes_answer_button))
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.no_answer_button))
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    DialogueBar(text = stringResource(id = R.string.question_dialogue1))
}
