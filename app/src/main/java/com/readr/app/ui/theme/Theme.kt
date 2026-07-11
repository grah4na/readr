package com.readr.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryYellow,
    secondary = DarkGreen,
    tertiary = SageGreen,
    background = DarkCharcoal,
    surface = DarkCharcoal,
    onPrimary = Black,
    onSecondary = White,
    onTertiary = White,
    onBackground = OffWhite,
    onSurface = OffWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryYellow,
    onPrimary = Black,
    secondary = DarkGreen,
    onSecondary = White,
    tertiary = SageGreen,
    onTertiary = White,
    background = OffWhite,
    onBackground = DarkCharcoal,
    surface = SoftBeige,
    onSurface = DarkCharcoal,
    surfaceVariant = LightSage,
    onSurfaceVariant = DarkCharcoal,
    outline = MediumGrey
)

@Composable
fun ReadrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
