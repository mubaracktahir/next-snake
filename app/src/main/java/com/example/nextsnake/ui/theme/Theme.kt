package com.example.nextsnake.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define the color scheme using the custom colors
private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    secondary = Red,
    tertiary = Yellow,
    background = BoardBackground, // Or a darker variant if preferred for dark theme
    surface = ControlBackground,
    onPrimary = TextColor,
    onSecondary = TextColor,
    onTertiary = Color.Black, // Text on Yellow accent
    onBackground = TextColor, // Or Color.Black depending on background
    onSurface = TextColor
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGreen,
    secondary = Red,
    tertiary = Yellow,
    background = BoardBackground,
    surface = ControlBackground,
    onPrimary = TextColor,
    onSecondary = TextColor,
    onTertiary = Color.Black, // Text on Yellow accent
    onBackground = Color.Black, // Text on Board Background
    onSurface = TextColor // Text on Control Background

    /* Other default colors to override if needed
    surfaceVariant = ...,
    outline = ...,
    */
)

@Composable
fun NextSnakeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // We are disabling dynamic color for a consistent Snake theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Always use our custom LightColorScheme for this game
        // darkTheme -> DarkColorScheme // Can enable this if a dark mode variant is desired
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Use primary color for status bar
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme // Adjust icon color based on theme
             // Optional: Set navigation bar color too
            window.navigationBarColor = colorScheme.surface.toArgb() // Use control background for nav bar
             WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assuming Typography.kt exists
        shapes = Shapes,         // Assuming Shapes.kt exists
        content = content
    )
}
