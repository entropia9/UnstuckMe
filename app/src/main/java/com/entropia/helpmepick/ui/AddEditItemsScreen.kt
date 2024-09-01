package com.entropia.helpmepick.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.bottomsheet.EntryBottomSheet
import com.entropia.helpmepick.ui.custom.CategoriesRow
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import com.entropia.helpmepick.ui.theme.Shapes
import com.entropia.helpmepick.ui.theme.Typography
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction2

object AddEditItemsScreenDestination : NavigationDestination {
    override val route: String
        get() = "add_edit_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemsScreen(
    viewModel: ItemsListViewModel,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.categoriesItemUiState.collectAsState()
    val editUiState = viewModel.addEditUiState.collectAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
        )
    )

    val scope = rememberCoroutineScope()

    EntryBottomSheet(
        itemsListViewModel = viewModel,
        sheetScaffoldState = bottomSheetScaffoldState,
        categories = uiState.value.categories,
        onCancel = {
            scope.launch {
                bottomSheetScaffoldState.bottomSheetState.hide()

            }
        },
        onSubmit = {
            scope.launch {
                bottomSheetScaffoldState.bottomSheetState.hide()
            }
        }) {
        Scaffold(topBar = {
            TopAppBar(
                title = stringResource(id = R.string.items),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }, floatingActionButton = {
            if (!editUiState.value.isEdited) {
                AddItemFAB(onClick = { scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } })
            }
        },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                CategoriesRow(
                    categories = uiState.value.categories,
                    currentCategory = uiState.value.currentCategory,
                    onAllClick = viewModel::showAllItems,
                    onCompletedClick = viewModel::showCompleted,
                    onCategoryClick = viewModel::showCurrentCategory,
                    onRemoveCategory = viewModel::removeCategory,
                    onRenameCategory = viewModel::renameCategory
                )
                ItemsList(
                    uiState = uiState,
                    viewModel = viewModel,
                    onEditItem = viewModel::updateEditedItem,
                    updateItem = viewModel::updateItem,
                    deleteItem = viewModel::deleteItem,
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }

}


@Composable
fun ItemsList(
    uiState: State<CategoryUiState>,
    viewModel: ItemsListViewModel,
    updateItem: (Item) -> Unit,
    onEditItem: KFunction2<String, String, Unit>,
    deleteItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 400.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        items(uiState.value.currentItems) { item ->
            if (uiState.value.currentCategory != stringResource(id = R.string.completed)) {
                ItemCard(
                    viewModel = viewModel,
                    updateItem = updateItem,
                    onEditItem = onEditItem,
                    deleteItem = deleteItem,
                    item = item,
                    modifier = Modifier
                )
            } else {
                CompletedItemCard(item = item)
            }

        }
    }
}


@Composable
fun ItemCard(
    viewModel: ItemsListViewModel,
    updateItem: (Item) -> Unit,
    onEditItem: KFunction2<String, String, Unit>,
    deleteItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
    item: Item,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var isEditable by remember {
        mutableStateOf(false)
    }

    if (showDialog) {
        DeleteDialog(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                deleteItem(item)
                showDialog = false
            },
            item = item,
        )
    }

    Box(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                ), shape = Shapes.medium
            )
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                            bottom = dimensionResource(
                                id = R.dimen.padding_small
                            )
                        )
                ) {
                    TextRow(
                        inputLabel = stringResource(R.string.name),
                        fieldValue = if (!isEditable) item.name else viewModel.editedItem.first,
                        onValueChange = {
                            viewModel.updateEditedItem(
                                it,
                                viewModel.editedItem.second
                            )
                        },
                        isEditable = isEditable,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    )
                    TextRow(
                        inputLabel = stringResource(R.string.category),
                        fieldValue = if (!isEditable) item.category else viewModel.editedItem.second,
                        onValueChange = {
                            viewModel.updateEditedItem(
                                viewModel.editedItem.first,
                                it
                            )
                        },
                        isEditable = isEditable,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    )
                }
                Column(
                    modifier = modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EditIcons(
                        isEditable,
                        onCheckClicked = {
                            viewModel.updateIsBeingEdited(false)
                            isEditable = false
                            updateItem(
                                item.copy(
                                    name = viewModel.editedItem.first,
                                    category = viewModel.editedItem.second
                                )
                            )
                        },
                        onClearClicked = {
                            viewModel.updateIsBeingEdited(false)
                            isEditable = false
                        },
                        onEditClicked = {
                            viewModel.updateIsBeingEdited(true)
                            isEditable = true
                            onEditItem(item.name, item.category)
                        }
                    )
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = "Delete",
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .clickable {
                                showDialog = true
                            }
                    )
                }
            }
            CompleteTab(
                completeItem = {
                    updateItem(
                        item.copy(completed = true)
                    )
                },
                modifier = modifier.clip(
                    RoundedCornerShape(
                        bottomEnd = 20.dp, bottomStart = 40.dp
                    )
                ),
                visible = viewModel.categoriesItemUiState.value.currentCategory != stringResource(id = R.string.completed)
            )
        }

    }
}

@Composable
fun CompletedItemCard(
    modifier: Modifier = Modifier,
    item: Item,
) {
    Box(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                ), shape = Shapes.medium
            )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_large))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.name, style = Typography.headlineMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.category_completed, item.category),
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.padding_medium)
                    ),
                    style = Typography.bodyLarge, fontWeight = FontWeight.ExtraBold
                )
            }

            Column(Modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))) {
                Text(
                    text = stringResource(id = R.string.regular_mode_completed, item.timesPicked),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    text = stringResource(id = R.string.battle_mode_completed, item.battleWins),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
                )

            }

        }

    }

}

@Composable
private fun EditIcons(
    isEditable: Boolean,
    onCheckClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onEditClicked: () -> Unit,
) {

    if (isEditable) {
        Row {
            Icon(
                imageVector = Icons.Default.Check, contentDescription = "Edit",
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .clickable {
                        onCheckClicked()
                    }
            )
            Icon(
                imageVector = Icons.Default.Clear, contentDescription = "Edit",
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .clickable {
                        onClearClicked()
                    }
            )
        }
    } else {
        Icon(
            imageVector = Icons.Default.Edit, contentDescription = "Edit",
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .clickable {
                    onEditClicked()
                }
        )

    }
}

@Composable
fun CompleteTab(
    modifier: Modifier = Modifier,
    completeItem: (() -> Unit),
    visible: Boolean,
) {
    if (visible) {
        Box(
            modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Completed?",

                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
                Button(
                    onClick = { completeItem() },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }
        }
    }
}


@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    item: Item,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text = { Text(stringResource(id = R.string.delete_confirmation, item.name)) },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = stringResource(id = R.string.yes_answer_button))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.no_answer_button))
            }
        },
        shape = RoundedCornerShape(20)

    )
}

@Composable
fun TextRow(
    inputLabel: String,
    fieldValue: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) { val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        modifier = modifier,
        value = fieldValue,
        enabled = isEditable,
        readOnly = !isEditable,
        onValueChange = onValueChange,
        label = { Text(inputLabel) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
        )
}

@Composable
fun AddItemFAB(
    onClick: () -> Unit, modifier: Modifier = Modifier,
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_item),
        )
    }
}


