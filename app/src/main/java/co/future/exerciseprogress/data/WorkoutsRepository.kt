package co.future.exerciseprogress.data

import android.content.Context
import co.future.exerciseprogress.data.models.Workout
import co.future.exerciseprogress.data.models.WorkoutSummary
import co.future.exerciseprogress.utils.extensions.nilUUIDString
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

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
    
    private val _workoutSummaries = mutableListOf<WorkoutSummary>()
    val workoutSummaries: List<WorkoutSummary> get() = _workoutSummaries
    
    init {
        loadWorkouts()
    }
    
    private fun loadWorkouts() {
        try {
            val workoutsJsonString = context.assets.open("workouts.json").bufferedReader().use { it.readText() }
            val shallowWorkouts = json.decodeFromString<List<Workout>>(workoutsJsonString)
            
            val workoutSummaryIDs = shallowWorkouts
                .flatMap { it.summaries }
                .mapNotNull { it.workoutSummaryID }
            
            for (workoutSummaryID in workoutSummaryIDs) {
                try {
                    val summaryJsonString = context.assets.open("summaries/$workoutSummaryID.json")
                        .bufferedReader().use { it.readText() }
                    val workoutSummary = json.decodeFromString<WorkoutSummary>(summaryJsonString)
                    
                    val filteredSummary = workoutSummary.copy(
                        setSummaries = workoutSummary.setSummaries.filter { setSummary ->
                            setSummary.exerciseSet?.id != nilUUIDString
                        }
                    )
                    _workoutSummaries.add(filteredSummary)
                } catch (e: Exception) {
                    println("Failed to load workout summary $workoutSummaryID: ${e.message}")
                }
            }
            
            val workoutIDs = shallowWorkouts.map { it.id }
            for (workoutID in workoutIDs) {
                try {
                    val workoutJsonString = context.assets.open("workouts/$workoutID.json")
                        .bufferedReader().use { it.readText() }
                    var workout = json.decodeFromString<Workout>(workoutJsonString)
                    workout = workout.copy(
                        summaries = _workoutSummaries.filter { it.workoutID == workoutID }
                    )
                    _workouts.add(workout)
                } catch (e: Exception) {
                    println("Failed to load workout $workoutID: ${e.message}")
                }
            }
            
            println("Loaded ${_workouts.size} workouts and ${_workoutSummaries.size} summaries")
        } catch (e: Exception) {
            println("Failed to load workouts: ${e.message}")
        }
    }
}