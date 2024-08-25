package com.entropia.helpmepick.ui.custom

import CustomButton
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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
    onRemoveCategory: (String) -> Unit,
    onRenameCategory: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openRenameDialog = remember { mutableStateOf(false) }
    val openRemoveDialog = remember { mutableStateOf(false) }
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
        items(list) { category ->
            when (category) {
                all -> {
                    Button(
                        onClick = {
                            onAllClick()
                        },
                        colors = determineButtonColors(currentCategory, category, list),
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Text(text = category)
                    }
                }

                completed -> if (onCompletedClick != null) {
                    Button(
                        onClick = {
                            onCompletedClick()
                        },
                        colors = determineButtonColors(currentCategory, category, list),
                        modifier = Modifier.padding(
                            end = dimensionResource(id = R.dimen.padding_small),
                            start = dimensionResource(id = R.dimen.padding_small)
                        )
                    ) {
                        Text(text = category)
                    }
                }

                else -> {
                    CustomButton(onClick = {
                        onCategoryClick(category)
                    },
                        colors = determineButtonColors(currentCategory, category, list),
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                        text = {
                            Text(text = category)
                        }) { hide ->
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.rename_category)) },
                            onClick = {
                                hide()
                                openRenameDialog.value = true
                            })
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.remove_category)) },
                            onClick = {
                                hide()
                                openRemoveDialog.value = true
                            })
                    }
                    AlertDialogs(
                        openRemoveDialog,
                        onRemoveCategory,
                        onRemoveDismiss = { openRemoveDialog.value = false },
                        category,
                        openRenameDialog,
                        onRenameCategory,
                        onRenameDismiss = { openRenameDialog.value = false },
                    )
                }
            }

        }
    }

}

@Composable
private fun AlertDialogs(
    openRemoveDialog: MutableState<Boolean>,
    onRemoveCategory: (String) -> Unit,
    onRemoveDismiss: () -> Unit,
    category: String,
    openRenameDialog: MutableState<Boolean>,
    onRenameCategory: (String, String) -> Unit,
    onRenameDismiss: () -> Unit,
) {
    when {
        openRemoveDialog.value -> RemoveAlert(
            onDismissRequest = onRemoveDismiss,
            onRemoveCategory = onRemoveCategory,
            category = category,
            icon = Icons.Default.Delete
        )
    }
    when {
        openRenameDialog.value -> RenameAlert(
            onDismissRequest = onRenameDismiss,
            onRenameCategory = onRenameCategory,
            category = category,
            icon = Icons.Default.Edit
        )
    }
}


@Composable
private fun RenameAlert(
    onDismissRequest: () -> Unit,
    onRenameCategory: (String, String) -> Unit,
    category: String,
    icon: ImageVector,
) {
    var newCategory by remember {
        mutableStateOf("")
    }
    AlertDialog(icon = {
        Icon(icon, contentDescription = null)
    }, title = {
        Text(text = stringResource(id = R.string.rename_category))
    }, text = {
        Column() {
            Text(
                text = stringResource(id = R.string.rename_category_dialog, category),
                textAlign = TextAlign.Justify
            )
            TextField(
                modifier = Modifier,
                value = newCategory,
                onValueChange = { newCategory = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
        }

    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            if (newCategory != "" && newCategory != category) onRenameCategory(
                category, newCategory
            )
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.confirm))
        }
    }, shape = ShapeDefaults.Medium, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.dismiss))
        }
    })

}


@Composable
private fun RemoveAlert(
    onDismissRequest: () -> Unit,
    onRemoveCategory: (String) -> Unit,
    category: String,
    icon: ImageVector,
) {
    AlertDialog(icon = {
        Icon(icon, contentDescription = null)
    }, title = {
        Text(text = stringResource(id = R.string.remove_category))
    }, text = {
        Text(
            text = stringResource(id = R.string.remove_category_confirmation, category),
            textAlign = TextAlign.Justify
        )
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onRemoveCategory(category)
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.yes_answer_button))
        }
    }, shape = ShapeDefaults.Medium, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.no_answer_button))
        }
    })

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