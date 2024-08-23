package com.entropia.helpmepick.ui.custom

import CustomButton
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.entropia.helpmepick.R

@Composable
fun CategoriesRow(
    categories: List<String>,
    currentCategory: String,
    onAllClick: () -> Unit,
    onCompletedClick: (() -> Unit)? = null,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val all = stringResource(id = R.string.all)
    val completed = stringResource(id = R.string.completed)
    val list = if (onCompletedClick != null) {
        listOf(all) + categories + listOf(completed)
    } else {
        listOf(all) + categories
    }

    LazyRow(
        state = listState, modifier = modifier
            .fadingEdge(
                FadingSide.LEFT,
                color = MaterialTheme.colorScheme.background,
                spec = tween(500),
                isVisible = listState.canScrollBackward
            )
            .fadingEdge(
                FadingSide.RIGHT,
                color = MaterialTheme.colorScheme.background,
                spec = tween(500),
                isVisible = listState.canScrollForward
            ), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(list) { item ->
            Button(
                onClick = {
                    when (item) {
                        all -> onAllClick()
                        completed -> if (onCompletedClick != null) {
                            onCompletedClick()
                        }

                        else -> onCategoryClick(item)
                    }
                },
                colors = determineButtonColors(currentCategory, item, list),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(text = item)
            }
        }
    }

}


@Composable
fun CategoriesRow(
    categories: List<String>,
    currentCategory: String,
    onAllClick: () -> Unit,
    onCompletedClick: (() -> Unit)? = null,
    onCategoryClick: (String) -> Unit,
    onLongCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val listState = rememberLazyListState()
    val all = stringResource(id = R.string.all)
    val completed = stringResource(id = R.string.completed)
    val list = if (onCompletedClick != null) {
        listOf(all) + categories + listOf(completed)
    } else {
        listOf(all) + categories
    }
    LazyRow(
        state = listState,
        modifier = modifier
            .fadingEdge(
                FadingSide.LEFT,
                color = MaterialTheme.colorScheme.background,
                spec = tween(500),
                isVisible = listState.canScrollBackward
            )
            .fadingEdge(
                FadingSide.RIGHT,
                color = MaterialTheme.colorScheme.background,
                spec = tween(500),
                isVisible = listState.canScrollForward
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(list) { item ->
            when (item) {
                all -> {
                    Button(
                        onClick = {
                            onAllClick()
                        },
                        colors = determineButtonColors(currentCategory, item, list),
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Text(text = item)
                    }
                }

                completed -> if (onCompletedClick != null) {
                    Button(
                        onClick = {
                            onCompletedClick()
                        },
                        colors = determineButtonColors(currentCategory, item, list),
                        modifier = Modifier.padding(
                            end = dimensionResource(id = R.dimen.padding_small),
                            start = dimensionResource(id = R.dimen.padding_small)
                        )
                    ) {
                        Text(text = item)
                    }
                }

                else -> {
                    CustomButton(
                        onClick = {
                            onCategoryClick(item)
                        },
                        onLongClick = { onLongCategoryClick() },
                        colors = determineButtonColors(currentCategory, item, list),
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Text(text = item)
                    }
                }
            }

        }
    }

}

@Composable
private fun determineButtonColors(
    currentCategory: String,
    item: String,
    list: List<String>,
) = if (currentCategory == item || (item == list[0] && currentCategory == "")) {
    ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
} else {
    ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
}