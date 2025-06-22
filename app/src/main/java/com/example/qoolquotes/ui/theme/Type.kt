package com.example.qoolquotes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.qoolquotes.R
import androidx.compose.ui.text.font.Font

val MerriweatherFont = FontFamily(Font(R.font.merriweather_variable))
val JetBrainsMonoFont = FontFamily(Font(R.font.jetbrainsmono_variable))
val PacificoFont = FontFamily(Font(R.font.pacifico_variable))

// Funkcja zwracająca typografię na podstawie wyboru użytkownika
fun getTypographyForFont(fontKey: String): Typography {
    val fontFamily = when (fontKey) {
        "Serif" -> MerriweatherFont
        "Monospace" -> JetBrainsMonoFont
        "Cursive" -> PacificoFont
        else -> FontFamily.Default
    }
    return Typography(
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        // Możesz dodać tu inne style np. titleLarge, bodyMedium itd.
    )
}


/*// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)*/