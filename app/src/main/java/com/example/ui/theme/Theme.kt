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
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MerchantBlue,
    onPrimary = Color.White,
    primaryContainer = MerchantBlueContainer,
    onPrimaryContainer = MerchantBlue,
    secondary = AmberCarton,
    onSecondary = Color.White,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = AmberCartonDark,
    tertiary = EmeraldProfit,
    onTertiary = Color.White,
    tertiaryContainer = EmeraldContainer,
    onTertiaryContainer = EmeraldProfitDark,
    background = SurfaceMuted,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = MerchantBlueLight,
    onPrimary = MerchantNavyDark,
    primaryContainer = MerchantBlue,
    onPrimaryContainer = Color.White,
    secondary = AmberCartonLight,
    onSecondary = MerchantNavyDark,
    secondaryContainer = AmberCartonDark,
    onSecondaryContainer = AmberContainer,
    tertiary = EmeraldProfitLight,
    onTertiary = MerchantNavyDark,
    tertiaryContainer = EmeraldProfitDark,
    onTertiaryContainer = EmeraldContainer,
    background = MerchantNavyDark,
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our branded B2B theme
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
