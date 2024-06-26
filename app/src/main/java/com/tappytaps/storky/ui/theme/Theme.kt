package com.tappytaps.storky.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFBAD1),
    primaryContainer = Color(0xF87BAD),
    secondary = Color(0xFFB9FFEA),
    tertiary = Color(0xFFFFBF99),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    background = Color(0xFF1A1114),
    surface = Color(0xFF1A1114),
    onPrimary = Color(0xFF640037),
    onSecondary = Color(0xFF00382D),
    onTertiary = Color(0xFF522300),
    onSurface = Color(0xFFF1DEE2),
    onSurfaceVariant = Color(0xFFDAC0C7),
    surfaceContainerHigh = Color(0xFF32272A) // Add surfaceContainerHigh color for dark theme
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF81B3),
    primaryContainer = Color(0xFFECF0),
    secondary = Color(0xFF17C1A1),
    tertiary = Color(0xFFD99F71),
    error = Color(0xFFFF897D),
    onError = Color(0xFFFFFFFF),
    background = Color(0xFFFFF8F8),
    surface = Color(0xFFFFF8F8),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onSurface = Color(0xFF23191C),
    onSurfaceVariant = Color(0xFF554248),   //Color(0xFF554248),
    surfaceContainerHigh = Color(0xFFF6E4E8) // Add surfaceContainerHigh color for light theme
)



@Composable
fun StorkyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
 //       typography = Typography,
        content = content,
        shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))  //With material3 the default shape used by the DropdownMenu is defined by the extraSmall attribute in the shapes, so I have to change it this way
    )
}

