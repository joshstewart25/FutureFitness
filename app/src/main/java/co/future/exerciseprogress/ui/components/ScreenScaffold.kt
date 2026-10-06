package co.future.exerciseprogress.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {
    Scaffold(modifier = modifier.fillMaxSize()) { scaffoldPadding ->
        val layoutDirection = LocalLayoutDirection.current

        Box(modifier = Modifier.padding(top = scaffoldPadding.calculateTopPadding())) {
            content(
                PaddingValues.Absolute(
                    left = scaffoldPadding.calculateLeftPadding(layoutDirection),
                    right = scaffoldPadding.calculateRightPadding(layoutDirection),
                    bottom = scaffoldPadding.calculateBottomPadding()
                )
            )
        }
    }
}
