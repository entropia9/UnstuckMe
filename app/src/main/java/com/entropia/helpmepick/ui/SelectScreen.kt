package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item


@Composable
fun AddButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(30),
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "add_button")
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
    Box(modifier = modifier) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.selected_icon), contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.zIndex(1f)
            )
        }
        Button(
            onClick = {
                selected = !selected
                selectOrDeselectItem(item)
            },
            shape = RoundedCornerShape(30),
            modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
        ) {
            Text(text = item.name)
        }
    }
}


@Preview
@Composable
fun ItemButtonPreview() {
    val item = Item(
        "CatFoodCalculator"
    )
    Row {
        AddButton()
        ItemButton(item, { (_) -> {} })

    }
}