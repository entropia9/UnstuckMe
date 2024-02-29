package com.entropia.helpmepick.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.bottomsheet.EntryBottomSheet
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object AddEditItemsScreenDestination : NavigationDestination {
    override val route: String
        get() = "add_edit_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemsScreen(viewModel: ItemsListViewModel, navigateUp: () -> Unit) {
    val uiState = viewModel.categoriesItemUiState.collectAsState()

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
            AddItemFAB(onClick = { scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } })
        }) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                CategoriesRow(
                    categories = uiState.value.categories,
                    currentCategory = uiState.value.currentCategory,
                    onAllClick = viewModel::showAllItems,
                    onCategoryClick = viewModel::showCurrentCategory
                )
                ItemsList(
                    items = uiState.value.currentItems,
                    updateItem = viewModel::updateItem,
                    deleteItem = viewModel::deleteItem,
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }

}


@Composable
fun CategoriesRow(
    categories: List<String>,
    currentCategory: String,
    onAllClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val list = listOf(stringResource(id = R.string.all)) + categories
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        items(list) { item ->
            Button(
                onClick = { if (item != list[0]) onCategoryClick(item) else onAllClick() },
                colors = if (currentCategory == item || (item == list[0] && currentCategory == "")) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(text = item)
            }
        }
    }

}


@Composable
fun ItemsList(
    items: List<Item>,
    updateItem: (Item) -> Unit,
    deleteItem: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items) { item ->
            ItemCard(
                updateItem = updateItem,
                deleteItem = deleteItem,
                item = item,
                name = item.name,
                category = item.category,
                modifier = Modifier
            )
        }
    }
}


@Composable
fun ItemCard(
    updateItem: (Item) -> Unit,
    deleteItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
    item: Item,
    name: String,
    category: String
) {
    var showDialog by remember {
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

    Card(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Column(modifier = Modifier.weight(0.7f)) {
                TextRow(
                    inputLabel = stringResource(R.string.name),
                    fieldValue = name,
                    onValueChange = { },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                )
                TextRow(
                    inputLabel = stringResource(R.string.category),
                    fieldValue = category,
                    onValueChange = { },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                )
            }
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Edit, contentDescription = "Edit",
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                        .clickable {
                            //TODO
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
    }
}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    item: Item,
) {
    AlertDialog(onDismissRequest = { onDismissRequest() },
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
        })
}

@Composable
fun TextRow(
    inputLabel: String,
    fieldValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = fieldValue,
        readOnly = true,
        onValueChange = onValueChange,
        label = { Text(inputLabel) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

        )
}

@Composable
fun AddItemFAB(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_item),
        )
    }
}

@Preview
@Composable
fun ItemCardPreview() {
    MaterialTheme {
        ItemCard(
            updateItem = {},
            deleteItem = {},
            item = Item(1, "Catfood Calculator", "Android"),
            name = "Catfood Calculator",
            category = "Android"
        )
    }
}
