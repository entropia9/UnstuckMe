package com.entropia.helpmepick.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.custom.Thumb
import com.entropia.helpmepick.ui.custom.Track
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
) {

    val openAlertDialog = remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(true) }

    when {
        openAlertDialog.value -> {
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
        AnimatedVisibility(
            visible = visible,
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_large))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.secondaryContainer
                            ), Offset(600f,600f), Offset.Infinite
                        ), shape = MaterialTheme.shapes.small
                    )){
                Column(
                    Modifier
                        .padding(it)
                        .padding(dimensionResource(id = R.dimen.padding_large)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(0.3f))
                    Track(
                        textRes = R.string.regular_mode, onDragComplete = {
                            visible = false
                            navigateToRegular()
                        },
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
                    ) {
                        Thumb(iconRes = R.drawable.regular_mode_icon)
                    }
                    Track(textRes = R.string.battle_mode, onDragComplete = {
                        visible = false
                        navigateToBattleMode()
                    },
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))) {
                        Thumb(iconRes = R.drawable.battle_mode_icon)
                    }
                    Track(textRes = R.string.add_edit_item, onDragComplete = {
                        visible = false
                        navigateToAddEditItems()
                    },
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))) {
                        Thumb(iconRes = R.drawable.edit_icon)
                    }
                    Track(textRes = R.string.stats, onDragComplete = {
                        visible = false
                        navigateToStats()
                    },
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))) {
                        Thumb(iconRes = R.drawable.stats_icon)
                    }
                    Spacer(modifier = Modifier.weight(0.3f))
                    AboutButton(onClick = { openAlertDialog.value = true })
                    Spacer(modifier = Modifier.weight(0.05f))
                }
            }

        }

    }

}

@Composable
private fun AboutButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .clickable {
                onClick()
            }

            .background(
                //color = MaterialTheme.colorScheme.primaryContainer,
                brush=Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primaryContainer
                    ), Offset.Zero, Offset(0f,100f)
                ),
                shape = ShapeDefaults.Medium
            ).padding(dimensionResource(id = R.dimen.padding_large)), horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Info, contentDescription = null,
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = stringResource(id = R.string.about),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
private fun AboutAlert(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = null)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(
                text = dialogText,
                textAlign = TextAlign.Justify
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
        },
        shape = ShapeDefaults.Medium,
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