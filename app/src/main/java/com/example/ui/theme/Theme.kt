package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = KomolaOrangeLight,
    onPrimary = Color.Black,
    primaryContainer = KomolaOrangeDark,
    secondary = AshCharcoalLight,
    onSecondary = Color.White,
    background = AshCharcoalDark,
    surface = AshCharcoal,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = KomolaOrange,
    onPrimary = Color.White,
    primaryContainer = KomolaOrangeLight,
    secondary = AshCharcoal,
    onSecondary = Color.White,
    background = AshBgGray,
    surface = AshCardBg,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to ensure our distinct orange and ash palette is prioritized
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
