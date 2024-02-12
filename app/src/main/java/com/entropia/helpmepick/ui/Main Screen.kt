package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R
import com.entropia.helpmepick.TopAppBar
import com.entropia.helpmepick.ui.navigation.NavigationDestination

object MainScreenDestination : NavigationDestination {
    override val route: String
        get() = "main_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigateToRegular: () -> Unit,
    navigateToBattleMode: () -> Unit,
    navigateToStats: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MenuButton(
                navigate = navigateToRegular,
                textRes = R.string.regular_mode,
                iconRes = R.drawable.regular_mode_icon,
            )
            MenuButton(
                navigate = navigateToBattleMode,
                textRes = R.string.battle_mode,
                iconRes = R.drawable.battle_mode_icon
            )
            MenuButton(
                navigate = navigateToStats,
                textRes = R.string.stats,
                iconRes = R.drawable.stats_icon
            )
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
    Button(onClick = { navigate() }, modifier = modifier.width(220.dp)) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = "",
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        Text(text = stringResource(textRes))
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(navigateToStats = { },
        navigateToRegular = {},
        navigateToBattleMode = {})
}