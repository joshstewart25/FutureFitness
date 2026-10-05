package co.future.exerciseprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.future.exerciseprogress.data.WorkoutsRepository
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WorkoutStats(
                        workoutsCount = workoutsRepository.workouts.size,
                        summariesCount = workoutsRepository.workoutSummaries.size,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutStats(
    workoutsCount: Int,
    summariesCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Workout Stats",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Workouts: $workoutsCount",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        
        Text(
            text = "Workout Summaries: $summariesCount",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutStatsPreview() {
    ExerciseProgressTheme {
        WorkoutStats(
            workoutsCount = 5,
            summariesCount = 12
        )
    }
}