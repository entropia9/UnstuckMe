package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item


@Composable
fun AddButton() {
    //TODO
}


@Composable
fun ItemButton(item: Item, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.selected_icon), contentDescription = null,
            modifier = Modifier.zIndex(1f)
        )
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(30),
            modifier = Modifier.padding(top=4.dp, start=8.dp, end=8.dp)
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
    ItemButton(item)
}