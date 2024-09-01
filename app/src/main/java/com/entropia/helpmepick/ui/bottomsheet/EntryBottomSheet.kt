package com.entropia.helpmepick.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.ItemsListViewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBottomSheet(
    itemsListViewModel: ItemsListViewModel,
    sheetScaffoldState: BottomSheetScaffoldState,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    categories: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    BottomSheetScaffold(
        modifier = modifier,
        sheetShape = RoundedCornerShape(topStartPercent = 20, topEndPercent = 20),
        scaffoldState = sheetScaffoldState,
        sheetContent = {
            Column {
                SheetHeader()
                SheetForm(
                    onCancel = {
                        onCancel()
                        keyboardController?.hide()
                    },
                    onSubmit = {
                        onSubmit()
                        keyboardController?.hide()
                    },
                    categories = categories,
                    validateItem = itemsListViewModel::validateItem,
                    addItem = itemsListViewModel::addItem,
                )
            }
        },
        sheetSwipeEnabled = false
    ) {
        content()
    }
}

@Composable
fun SheetForm(
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    categories: List<String>,
    validateItem: (Item) -> Boolean,
    addItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember {
        mutableStateOf("")
    }
    var category by remember {
        mutableStateOf("")
    }

    var entryValid by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextInputRow(
            inputLabel = stringResource(R.string.name),
            fieldValue = name,
            onValueChange = {
                name = it
                entryValid = true
            },
            imeAction = ImeAction.Next
        )
        SpinnerRow(
            inputLabel = stringResource(id = R.string.category),
            categories = categories,
            category = category,
            onValueChange = { category = it })
        if (!entryValid) {
            Text(
                text = stringResource(id = R.string.entry_invalid),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
        ButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onCancel = onCancel,
            onSubmit = {
                if (validateItem(Item(name = name, category = category))) {
                    addItem(Item(name = name, category = category))
                    onSubmit()
                    name = ""
                    entryValid = true
                } else {
                    entryValid = false
                }
            },
            submitButtonEnabled = name.isNotBlank() && category.isNotBlank()
        )
    }
}


@Composable
fun SheetHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            modifier = modifier.padding(8.dp),
            text = stringResource(R.string.bottom_sheet_headline),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        HorizontalDivider()
    }
}

@Composable
fun ButtonRow(
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    submitButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            border = null
        ) {
            Text(stringResource(android.R.string.cancel).uppercase(Locale.getDefault()))
        }
        Button(
            onClick = onSubmit,
            enabled = submitButtonEnabled
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
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    InputRow(inputLabel, modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = fieldValue,
            onValueChange = onValueChange,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )

        )
    }
}


@Composable
fun SpinnerRow(
    inputLabel: String,
    categories: List<String>,
    category: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    InputRow(inputLabel = inputLabel, modifier = modifier) {
        CategorySpinner(
            categories = categories,
            selectedOptionText = category,
            onValueChange = onValueChange
        )
    }
}

@Composable
fun InputRow(
    inputLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = inputLabel,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .weight(1f)
                .padding(end = 8.dp),
        )
        Box(modifier = Modifier.weight(2f)) {
            content()
        }
    }
}
