package co.future.exerciseprogress.data

import android.content.Context
import co.future.exerciseprogress.data.models.Workout
import co.future.exerciseprogress.data.models.WorkoutSummary
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject
import javax.inject.Singleton

private const val NIL_UUID_STRING = "00000000-0000-0000-0000-000000000000"

@Singleton
class WorkoutsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    
    private val _workouts = mutableListOf<Workout>()
    val workouts: List<Workout> get() = _workouts
    
    private val workoutSummaries = mutableListOf<WorkoutSummary>()
    
    init {
        loadWorkouts()
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    private fun loadWorkouts() {
        try {
            val shallowWorkouts = context.assets.open("workouts.json").use { stream ->
                json.decodeFromStream<List<Workout>>(stream)
            }

            val workoutSummaryIDs = shallowWorkouts
                .flatMap { it.summaries }
                .mapNotNull { it.workoutSummaryID }
            
            for (workoutSummaryID in workoutSummaryIDs) {
                try {
                    val workoutSummary = context.assets.open("summaries/$workoutSummaryID.json").use { stream ->
                        json.decodeFromStream<WorkoutSummary>(stream)
                    }

                    val filteredSummary = workoutSummary.copy(
                        setSummaries = workoutSummary.setSummaries.filter { setSummary ->
                            setSummary.exerciseSet?.id != NIL_UUID_STRING
                        }
                    )
                    workoutSummaries.add(filteredSummary)
                } catch (e: Exception) {
                    println("Failed to load workout summary $workoutSummaryID: ${e.message}")
                }
            }
            
            val workoutIDs = shallowWorkouts.map { it.id }
            for (workoutID in workoutIDs) {
                try {
                    var workout = context.assets.open("workouts/$workoutID.json").use { stream ->
                        json.decodeFromStream<Workout>(stream)
                    }
                    workout = workout.copy(
                        summaries = workoutSummaries.filter { it.workoutID == workoutID }
                    )
                    _workouts.add(workout)
                } catch (e: Exception) {
                    println("Failed to load workout $workoutID: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("Failed to load workouts: ${e.message}")
        }
    }
}