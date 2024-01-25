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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.theme.HelpMePickTheme

@Composable
fun SelectScreen(viewModel: ItemsListViewModel, modifier: Modifier = Modifier) {
    val uiState = viewModel.itemsListUiState.collectAsState()
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AddButton(
            addItem = viewModel::addItem, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    dimensionResource(id = R.dimen.padding_medium)
                )
        )
        if (uiState.value.itemsList.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.select),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            ItemsGrid(
                itemList = uiState.value.itemsList,
                viewModel = viewModel,
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.padding_medium)
                )
            )
            Button(onClick = { viewModel.selectAll() }) {
                Text(text = (stringResource(id = R.string.select_all)))
            }
            Text(text = stringResource(id = R.string.select_random))
            Text(
                text = "Selected: " + if (uiState.value.selectedItemsList.isNotEmpty()) viewModel.listSelectedItems() else "",
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (uiState.value.selectedItemsList.isNotEmpty()) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier) {
                Text(text = stringResource(id = R.string.done_button))
            }
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
fun AddButton(modifier: Modifier = Modifier, addItem: (Item) -> Unit) {
    var name by remember {
        mutableStateOf("")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        TextField(value = name,
            onValueChange = { name = it },
            singleLine = true,
            placeholder = { Text(text = "Enter name") })
        Button(
            onClick = {
                addItem(Item(name))
                name = ""
            },
            shape = RoundedCornerShape(30),
            modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
        ) {
            Text(text = "Add Item")
            Icon(Icons.Filled.Add, contentDescription = "add_button")
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
    Box(modifier = modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)){
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


@Preview
@Composable
fun ItemButtonPreview() {
    HelpMePickTheme {
        val item = Item(
            "CatFoodCalculator"
        )
        val items = listOf(item, item, item, item, item, item)
        Column {
            AddButton(
                addItem = {
                    @Suppress("UNUSED_EXPRESSION") ("name")
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Box(modifier = Modifier.weight(1f))

        }
    }
}

