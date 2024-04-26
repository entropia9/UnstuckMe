package com.entropia.helpmepick.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes= Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(
        topEnd = 20.dp, topStart = 40.dp,
        bottomEnd = 20.dp, bottomStart = 40.dp
    ),
    extraLarge = RoundedCornerShape(40)
)