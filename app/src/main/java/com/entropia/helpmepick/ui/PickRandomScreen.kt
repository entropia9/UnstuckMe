package com.entropia.helpmepick.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R

@Composable
fun PickRandom(viewModel: ItemsListViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = viewModel.pickItemUiState.currentPick?.name ?: " ")
        Button(onClick = {
            viewModel.pickRandomFromSelected()
        }) {
            Text(text = stringResource(id = R.string.help_me_pick_btn))
        }
    }

}




//TODO popup?