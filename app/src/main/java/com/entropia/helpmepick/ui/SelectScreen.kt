package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.theme.HelpMePickTheme


@Composable
fun ItemsGrid(itemList: List<Item>, modifier: Modifier = Modifier) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(itemList) { item ->
            ItemButton(item, { (_) -> {} }, modifier = Modifier.wrapContentSize())  //TODO
        }

    }
}

@Composable
fun AddButton(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        TextField(value = "", onValueChange = { }, placeholder = { Text(text = "Enter name") }
        )
        Button(
            onClick = { /*TODO*/ },
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
    selectOrDeselectItem: (item: Item) -> Unit,
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
                selectOrDeselectItem(item)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            ItemsGrid(itemList = items, modifier = Modifier.weight(0.15f))
            Box(modifier = Modifier.weight(1f))

        }
    }
}