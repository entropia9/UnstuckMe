package com.entropia.helpmepick.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item


@Composable
fun SelectScreen(
    viewModel: ItemsListViewModel,
    navigate: () -> Unit,
    navigateToAddEdit: ()->Unit,
    isEnabled: (Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.itemsListUiState.collectAsState()

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CategoriesRow(
            categories = viewModel.categoriesItemUiState.categories,
            currentCategory = viewModel.categoriesItemUiState.currentCategory,
            onAllClick = { viewModel.showAllItems() },
            onCategoryClick = viewModel::showCurrentCategory
        )

        if (uiState.value.itemsList.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.select),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            ItemsGrid(
                itemList = viewModel.categoriesItemUiState.currentItems,
                viewModel = viewModel,
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.padding_medium)
                )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_medium)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Button(
                            onClick = { viewModel.selectAll() },
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                        ) {
                            Text(text = (stringResource(id = R.string.select_all)))
                        }
                        Button(
                            onClick = { viewModel.clearAll() },
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                        ) {
                            Text(text = (stringResource(id = R.string.clear_all)))
                        }
                    }
                    if (uiState.value.itemsList.size > 2) {
                        SelectRandom(
                            viewModel::selectRandom, uiState.value.itemsList.size - 1
                        )
                    }

                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(){
            Button(onClick = { navigateToAddEdit() }) {
                Text(text = stringResource(id = R.string.add_edit_item))
            }
            Spacer(modifier = Modifier.weight(1f))
            DoneButton(
                navigate = { navigate() },
                isEnabled = isEnabled,
                size = uiState.value.selectedItemsList.size
            )
        }

    }
}

@Composable
fun DoneButton(navigate: () -> Unit, isEnabled: (Int) -> Boolean, size: Int) {
    Button(
        onClick = { navigate() },
        enabled = isEnabled(size)
    ) {
        Text(text = stringResource(id = R.string.done_button))
    }
}

@Composable
private fun SelectRandom(
    selectFunction: (Int) -> Unit,
    upperLimit: Int,
    modifier: Modifier = Modifier,
    lowerLimit: Int = 2
) {
    var number by remember {
        mutableIntStateOf(2)
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(
            text = stringResource(id = R.string.select_random) + " " + number.toString()
        )
        Column {
            Icon(imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier.clickable {
                    if (number < upperLimit) number++
                })
            Icon(imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.clickable {
                    if (number > lowerLimit) number--
                })
        }
        Text(text = stringResource(id = R.string.select_random2))

        Button(onClick = { selectFunction(number) }) {
            Text(text = stringResource(id = R.string.select_btn))
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemsGrid(itemList: List<Item>, viewModel: ItemsListViewModel, modifier: Modifier = Modifier) {
    var visible by remember {
        mutableStateOf(true)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            Text(text = if (visible) stringResource(id = R.string.hide_list) else stringResource(id = R.string.show_list))
            Icon(imageVector = if (visible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Dropdown",
                modifier = Modifier.clickable {
                    visible = !visible
                })
        }
        if (visible) {
            FlowRow(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                itemList.forEach { item ->
                    ItemButton(
                        item,
                        viewModel::selectItem,
                        viewModel::deselectItem,
                        modifier = Modifier.wrapContentSize(),
                        selected = viewModel.itemsListUiState.value.selectedItemsList.contains(item)
                    )
                }

            }
        }
    }
}

@Composable
fun ItemButton(
    item: Item,
    selectItem: (item: Item) -> Unit,
    deselectItem: (item: Item) -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {

    val colors = if (selected) ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) else ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
    Box(modifier = modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.selected_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.zIndex(1f)
            )
        }
        Button(
            onClick = {
                if (!selected) selectItem(item) else deselectItem(item)
            },
            colors = colors,
            shape = RoundedCornerShape(30),
        ) {
            Text(text = item.name)
        }
    }

}

