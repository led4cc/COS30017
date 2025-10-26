package com.example.musicrentalapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.unit.sp
import com.example.musicrentalapp.R

// 🎵 Add handwriting font family
val FancyFont = FontFamily(
    Font(R.font.dancing_script_regular, FontWeight.Normal),
    Font(R.font.dancing_script_bold, FontWeight.Bold)
)
// Reuse normal text for the rest
val Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FancyFont,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = FancyFont,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )
    // keep other defaults (bodyLarge, labelSmall, etc.)
)


val AppTitleStyle = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.2.sp
)

val PriceStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium
)


