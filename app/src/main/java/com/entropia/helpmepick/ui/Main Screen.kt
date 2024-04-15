package com.entropia.helpmepick.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.navigation.NavigationDestination
import com.entropia.helpmepick.ui.theme.HelpMePickTheme

object MainScreenDestination : NavigationDestination {
    override val route: String
        get() = "main_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigateToAddEditItems: () -> Unit,
    navigateToRegular: () -> Unit,
    navigateToBattleMode: () -> Unit,
    navigateToStats: () -> Unit,
    modifier: Modifier = Modifier
) {  val openAlertDialog = remember { mutableStateOf(false) }
    when{
        openAlertDialog.value->{
            AboutAlert(
                onDismissRequest = { openAlertDialog.value = false },
                dialogTitle = stringResource(id = R.string.about),
                dialogText = stringResource(id = R.string.about_text),
                icon = Icons.Default.Info
            )
        }
    }
    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = MainScreenDestination.titleRes),
                canNavigateBack = false
            )
        }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(dimensionResource(id = R.dimen.padding_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.3f))
            Row() {
                MenuButton(
                    navigate = navigateToRegular,
                    textRes = R.string.regular_mode,
                    iconRes = R.drawable.regular_mode_icon,
                    modifier = Modifier
                        .weight(1f)
                        .height(180.dp)
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_small),
                            bottom = dimensionResource(id = R.dimen.padding_small)
                        )
                )
                MenuButton(
                    navigate = navigateToBattleMode,
                    textRes = R.string.battle_mode,
                    iconRes = R.drawable.battle_mode_icon,
                    modifier = Modifier
                        .weight(1f)
                        .height(180.dp)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_small),
                            bottom = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }
            Spacer(modifier = Modifier.weight(0.05f))
            MenuButton(
                navigate = navigateToAddEditItems,
                textRes = R.string.add_edit_item,
                iconRes = R.drawable.edit_icon,
                modifier = Modifier.fillMaxWidth()
            )

            MenuButton(
                navigate = navigateToStats,
                textRes = R.string.stats,
                iconRes = R.drawable.stats_icon,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(0.3f))
            AboutButton(onClick={openAlertDialog.value=true})
            Spacer(modifier = Modifier.weight(0.05f))
        }
    }

}

@Composable
private fun MenuButton(
    navigate: () -> Unit,
    textRes: Int,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { navigate() },
        modifier = modifier.width(220.dp),
        shape = ShapeDefaults.Medium
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = "",
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        Text(text = stringResource(textRes))
    }
}

@Composable
private fun AboutButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(modifier = modifier
        .wrapContentSize()
        .clickable {
            onClick()
        }) {
        Icon(
            imageVector = Icons.Filled.Info, contentDescription = null,
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        Text(text = stringResource(id = R.string.about))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutAlert(onDismissRequest: () -> Unit,
                       dialogTitle: String,
                       dialogText: String,
                       icon: ImageVector){
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = null)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText,
                 textAlign = TextAlign.Justify)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.dismiss))
            }
        }
    )

}
@Preview
@Composable
fun MainScreenPreview() {
    HelpMePickTheme {
        MainScreen(navigateToAddEditItems = {},
            navigateToStats = { },
            navigateToRegular = {},
            navigateToBattleMode = {})
    }
}