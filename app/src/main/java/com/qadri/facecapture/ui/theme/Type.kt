package com.qadri.facecapture.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.qadri.facecapture.R

private val Poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_black, weight = FontWeight.Black),
    Font(R.font.poppins_bold, weight = FontWeight.Bold),
    Font(R.font.poppins_italic, style = FontStyle.Italic),
    Font(R.font.poppins_light, weight = FontWeight.Light),
)

// Set of Material typography styles to start with
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)