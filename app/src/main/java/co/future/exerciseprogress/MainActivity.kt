package co.future.exerciseprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.future.exerciseprogress.ui.navigation.AppNavHost
import co.future.exerciseprogress.ui.theme.ExerciseProgressTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExerciseProgressTheme {
                AppNavHost()
            }
        }
    }
}
