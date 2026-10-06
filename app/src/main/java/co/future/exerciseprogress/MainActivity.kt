package co.future.exerciseprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.future.exerciseprogress.data.WorkoutsRepository
import co.future.exerciseprogress.ui.navigation.AppNavHost
import co.future.exerciseprogress.ui.theme.ExerciseProgressTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var workoutsRepository: WorkoutsRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Log to verify repository is injected and initialized
        println("WorkoutsRepository injected: ${::workoutsRepository.isInitialized}")
        println("Workouts loaded: ${workoutsRepository.workouts.size} workouts")
        println("Workout summaries loaded: ${workoutsRepository.workoutSummaries.size} summaries")

        setContent {
            ExerciseProgressTheme {
                AppNavHost()
            }
        }
    }
}