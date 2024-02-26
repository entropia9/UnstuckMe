package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.bottomsheet.EntryBottomSheet
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.util.Locale

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
fun ItemsList(items: List<Item>, updateItem: (Item) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items) { item ->
            ItemCard(
                updateItem = updateItem,
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
    modifier: Modifier = Modifier,
    item: Item,
    name: String,
    category: String
) {

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
                    onValueChange = {  },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                )
                TextRow(
                    inputLabel = stringResource(R.string.category),
                    fieldValue = category,
                    onValueChange = { },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                )
            }
            ButtonRow(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .align(Alignment.CenterVertically), onSubmit = {
                    updateItem(Item(id = item.id, name = name, category = category))
                }, submitButtonEnabled = name.isNotEmpty()
            )
        }

    }


}


@Composable
fun ButtonRow(
    onSubmit: () -> Unit, submitButtonEnabled: Boolean, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Button(
            onClick = onSubmit, enabled = submitButtonEnabled
        ) {
            Text(stringResource(R.string.save).uppercase(Locale.getDefault()))
        }
    }
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
