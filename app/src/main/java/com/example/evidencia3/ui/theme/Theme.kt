package com.example.evidencia3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Import the new color palette
import com.example.evidencia3.ui.theme.RedPrimary
import com.example.evidencia3.ui.theme.RedDark
import com.example.evidencia3.ui.theme.RedLight
import com.example.evidencia3.ui.theme.White
import com.example.evidencia3.ui.theme.Black

private val DarkColorScheme = darkColorScheme(
    primary = RedPrimary,
    onPrimary = White,
    secondary = RedLight,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = RedDark,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = RedPrimary,
    onPrimary = White,
    secondary = RedLight,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

@Composable
fun Evidencia3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // force our custom palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}