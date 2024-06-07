package com.entropia.helpmepick.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.entropia.helpmepick.R


val Dosis = FontFamily(
    Font(R.font.dosis_regular, FontWeight.Normal)
)


val AmaticSC = FontFamily(
    Font(R.font.amaticsc_regular, FontWeight.Normal),
    Font(R.font.amaticsc_bold, FontWeight.Bold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Dosis,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Dosis,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AmaticSC,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Dosis,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

)