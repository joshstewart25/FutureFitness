package co.future.exerciseprogress.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}