package com.entropia.helpmepick.ui.custom

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.entropia.helpmepick.R


@Composable
fun ThemeButton(
    isDarkMode: Boolean,
    onDragComplete: () -> Unit,
    onDragReverse: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        Box(modifier.width(120.dp)) {
            Track(
                initialValue =
                when (isDarkMode) {
                    true -> DragAnchors.End
                    else -> DragAnchors.Start
                },
                onDragComplete = { onDragComplete() },
                onDragReverse = { onDragReverse() },
                contentSize = 40.dp,
                shape = RoundedCornerShape(60),
                thumb = {
                    Thumb(shape = RoundedCornerShape(60))
                }) {
            }
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.light_mode),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(start = 9.dp)
                        .size(30.dp)

                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.dark_mode),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(end = 9.dp)
                        .size(30.dp)


                )
            }

        }
    }



}
