package com.entropia.helpmepick.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.navigation.NavigationDestination

object MainScreenDestination : NavigationDestination {
    override val route: String
        get() = "main_screen"
    override val titleRes: Int
        get() = R.string.app_name

}

@Composable
fun MainScreen(navigateToStats:()->Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.regular_mode_icon),
                contentDescription = ""
            )
            Text(text = stringResource(id = R.string.regular))
        }
    }
}