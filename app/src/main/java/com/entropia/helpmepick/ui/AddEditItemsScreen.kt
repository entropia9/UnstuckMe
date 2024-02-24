package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.navigation.NavigationDestination
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
    val uiState = viewModel.categoriesItemUiState
    Scaffold(topBar = {
        TopAppBar(
            title = stringResource(id = R.string.items),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            CategoriesRow(
                categories = uiState.categories,
                currentCategory = uiState.currentCategory,
                onAllClick = { viewModel.showAllItems() },
                onCategoryClick = viewModel::showCurrentCategory
            )
            ItemsList(
                items = uiState.currentItems,
                updateItem = viewModel::updateItem,
                modifier = Modifier.weight(1f)
            )
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
    Column(modifier = modifier.fillMaxSize()) {
        items.forEach { item ->
            Form(
                updateItem = updateItem,
                item = item,
                modifier = Modifier
            )
        }
    }
}


@Composable
fun Form(
    updateItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
    item: Item,
) {
    var name by remember {
        mutableStateOf(item.name)
    }
    var category by remember {
        mutableStateOf(item.category)
    }

    Card(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Column(modifier = Modifier.weight(0.7f)) {
                TextInputRow(
                    inputLabel = stringResource(R.string.name),
                    fieldValue = name,
                    onValueChange = { name = it },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                )
                TextInputRow(
                    inputLabel = stringResource(R.string.category),
                    fieldValue = category,
                    onValueChange = { category = it },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                )
            }
            ButtonRow(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .align(Alignment.CenterVertically),
                onSubmit = {
                    updateItem(Item(id = item.id, name = name, category = category))
                },
                submitButtonEnabled = name.isNotEmpty()
            )
        }

    }


}


@Composable
fun ButtonRow(
    onSubmit: () -> Unit,
    submitButtonEnabled: Boolean,
    modifier: Modifier = Modifier
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
fun TextInputRow(
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

