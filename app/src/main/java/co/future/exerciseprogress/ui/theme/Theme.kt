package co.future.exerciseprogress.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FutureGreen,
    onPrimary = FutureBackground,
    secondary = FutureBlue,
    onSecondary = FutureTextPrimary,
    tertiary = FutureOrange,
    onTertiary = FutureBackground,
    background = FutureBackground,
    onBackground = FutureTextPrimary,
    surface = FutureSurface,
    onSurface = FutureTextPrimary,
    surfaceVariant = FutureSurfaceHigh,
    onSurfaceVariant = FutureTextSecondary,
    outline = FutureOutline
)

private val LightColorScheme = lightColorScheme(
    primary = FutureGreenDark,
    onPrimary = FutureLightSurface,
    secondary = FutureBlue,
    onSecondary = FutureLightSurface,
    tertiary = FutureOrange,
    onTertiary = FutureBackground,
    background = FutureLightBackground,
    onBackground = FutureLightTextPrimary,
    surface = FutureLightSurface,
    onSurface = FutureLightTextPrimary
)

@Composable
fun ExerciseProgressTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default so the Future brand colors show on Android 12+ too
    dynamicColor: Boolean = false,
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