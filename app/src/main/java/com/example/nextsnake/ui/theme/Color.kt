package com.example.nextsnake.ui.theme

import androidx.compose.ui.graphics.Color

// Define your light theme colors based on globals.css
val LightPrimary = Color(0xFF006400) // Dark Green (approx. HSL 120, 100%, 20% -> RGB)
val LightOnPrimary = Color(0xFFFFFFFF) // White text on primary
val LightPrimaryContainer = Color(0xFF98FB98) // Pale Green (adjust as needed)
val LightOnPrimaryContainer = Color(0xFF002108) // Darker Green text

val LightSecondary = Color(0xFFDC143C) // Crimson Red (approx. HSL 348, 83%, 47% -> RGB) - close to food red
val LightOnSecondary = Color(0xFFFFFFFF) // White text on secondary
val LightSecondaryContainer = Color(0xFFFFDAD6) // Light Red/Pink
val LightOnSecondaryContainer = Color(0xFF410002) // Dark Red text

val LightTertiary = Color(0xFF386666) // Tealish (Example) - Head color
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFBBECEC)
val LightOnTertiaryContainer = Color(0xFF002020)


val LightError = Color(0xFFBA1A1A) // Standard Error Red
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

val LightBackground = Color(0xFFFFFFFF) // White background
val LightOnBackground = Color(0xFF1A1C19) // Dark text
val LightSurface = Color(0xFFFDFDF6) // Slightly off-white surface
val LightOnSurface = Color(0xFF1A1C19)
val LightSurfaceVariant = Color(0xFFDEE5D9) // Muted variant
val LightOnSurfaceVariant = Color(0xFF424940) // Muted text
val LightOutline = Color(0xFF727970) // Border color
val LightInverseOnSurface = Color(0xFFF0F1EC)
val LightInverseSurface = Color(0xFF2F312D)
val LightInversePrimary = Color(0xFF66DD78) // Lighter Green for dark inverse
val LightSurfaceTint = LightPrimary // Often same as primary
val LightOutlineVariant = Color(0xFFC2C9BE) // Lighter border
val LightScrim = Color(0xFF000000) // Scrim color (for modals, etc.)

// Define your dark theme colors based on globals.css .dark
val DarkPrimary = Color(0xFF66DD78) // Lighter Green (approx. HSL 130, 65%, 63% -> RGB)
val DarkOnPrimary = Color(0xFF003912) // Very Dark Green text
val DarkPrimaryContainer = Color(0xFF00501F) // Dark Green container
val DarkOnPrimaryContainer = Color(0xFF98FB98) // Light Green text on dark container

val DarkSecondary = Color(0xFFFFB3AD) // Light Red (approx. HSL 6, 100%, 84% -> RGB) - Food color dark mode
val DarkOnSecondary = Color(0xFF690005) // Dark Red text
val DarkSecondaryContainer = Color(0xFF93000A) // Dark Red container
val DarkOnSecondaryContainer = Color(0xFFFFDAD6) // Light Red text on dark container

val DarkTertiary = Color(0xFFA0CFCE) // Lighter Tealish (Example) - Head color dark
val DarkOnTertiary = Color(0xFF003737)
val DarkTertiaryContainer = Color(0xFF1E4E4E)
val DarkOnTertiaryContainer = Color(0xFFBBECEC)

val DarkError = Color(0xFFFFB4AB) // Light Error Red
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkBackground = Color(0xFF1A1C19) // Dark background (adjust darkness)
val DarkOnBackground = Color(0xFFE2E3DD) // Light text
val DarkSurface = Color(0xFF1A1C19) // Dark surface
val DarkOnSurface = Color(0xFFE2E3DD)
val DarkSurfaceVariant = Color(0xFF424940) // Muted dark variant
val DarkOnSurfaceVariant = Color(0xFFC2C9BE) // Muted light text
val DarkOutline = Color(0xFF8C9389) // Border color dark
val DarkInverseOnSurface = Color(0xFF1A1C19)
val DarkInverseSurface = Color(0xFFE2E3DD)
val DarkInversePrimary = Color(0xFF006E2D) // Darker Green for light inverse
val DarkSurfaceTint = DarkPrimary // Often same as primary
val DarkOutlineVariant = Color(0xFF424940) // Darker border variant
val DarkScrim = Color(0xFF000000)
