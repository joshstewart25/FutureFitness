package co.future.exerciseprogress.ui.workoutdetail

import androidx.annotation.StringRes
import co.future.exerciseprogress.R
import co.future.exerciseprogress.data.models.ExerciseSetCompletionState

data class WorkoutDetailUiState(
    val name: String,
    val description: String,
    val isRestDay: Boolean,
    // Null when the workout was never started (for example, it was missed), so there is nothing to summarize.
    val summary: WorkoutSummaryUiState?,
    val expandedSectionIDs: Set<String> = emptySet()
)

// Any value that is null was not recorded for this workout, and its part of the dashboard is left out.
data class WorkoutSummaryUiState(
    val isCompleted: Boolean,
    val heartRateChart: HeartRateChartUiState?,
    val averageHeartRate: Int?,
    val maxHeartRate: Int?,
    val activeEnergyBurned: Int?,
    val durationMinutes: Int?,
    val notes: String?,
    val difficulty: DifficultyUiState?,
    val location: LocationUiState?,
    val sections: List<SetSectionUiState>
)

data class HeartRatePoint(
    val secondsSinceStart: Int,
    val bpm: Int
)

data class HeartRateChartUiState(
    val points: List<HeartRatePoint>,
    val peak: HeartRatePoint,
    val lowestBpm: Int
)

enum class DifficultyLevel(@StringRes val labelRes: Int) {
    EASY(R.string.difficulty_easy),
    MODERATE(R.string.difficulty_moderate),
    HARD(R.string.difficulty_hard)
}

data class DifficultyUiState(
    // From 0 (easiest) to 1 (hardest).
    val value: Float,
    val level: DifficultyLevel
)

data class LocationUiState(
    val latitude: Double,
    val longitude: Double
)

data class SetSectionUiState(
    val id: String,
    val name: String,
    val completedSetCount: Int,
    val totalSetCount: Int,
    val exercises: List<ExerciseUiState>
)

data class ExerciseUiState(
    val name: String,
    val sets: List<SetResultUiState>
)

// Only the values that apply to the kind of set are filled in: reps for rep sets, duration for timed sets, and so on.
data class SetResultUiState(
    val weight: Float?,
    val weightUnit: String,
    val reps: Int?,
    val durationSeconds: Int?,
    val distanceMeters: Double?,
    val completionState: ExerciseSetCompletionState
)
