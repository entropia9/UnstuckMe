package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.theme.HelpMePickTheme

@Composable
fun SelectScreen(viewModel: ItemsListViewModel, modifier: Modifier = Modifier) {
    val uiState = viewModel.itemsListUiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        AddButton(
            addItem = viewModel::addItem, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    dimensionResource(id = R.dimen.padding_medium)
                )
        )
        ItemsGrid(itemList = uiState.value.itemsList, viewModel = viewModel)

        Text(text = "Picked: " + uiState.value.selectedItemsList.toString())
    }
}

@Composable
fun ItemsGrid(itemList: List<Item>, viewModel: ItemsListViewModel, modifier: Modifier = Modifier) {
    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxHeight(0.2f)
    ) {
        items(itemList) { item ->
            ItemButton(
                item,
                viewModel::selectItem,
                viewModel::deselectItem,
                modifier = Modifier.wrapContentSize()
            )
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
        TextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            placeholder = { Text(text = "Enter name") }
        )
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
    modifier: Modifier = Modifier
) {
    var selected by remember {
        mutableStateOf(
            false
        )
    }
    val colors = if (selected) ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) else ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
    Box(modifier = modifier) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.selected_icon), contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.zIndex(1f)
            )
        }
        Button(
            onClick = {
                selected = !selected
                if (selected) selectItem(item) else deselectItem(item)
            },
            colors = colors,
            shape = RoundedCornerShape(30),
            modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
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
                addItem = { ("name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Box(modifier = Modifier.weight(1f))

        }
    }
}

