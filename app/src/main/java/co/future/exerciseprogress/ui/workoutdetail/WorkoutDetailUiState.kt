package co.future.exerciseprogress.ui.workoutdetail

import co.future.exerciseprogress.data.models.enums.ExerciseSetCompletionState
import co.future.exerciseprogress.ui.workoutdetail.enums.DifficultyLevel

data class WorkoutDetailUiState(
    val name: String,
    val description: String,
    val isRestDay: Boolean,
    val summary: WorkoutSummaryUiState?,
    val expandedSectionIDs: Set<String> = emptySet()
)

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

data class DifficultyUiState(
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

data class SetResultUiState(
    val weight: Float?,
    val weightUnit: String,
    val reps: Int?,
    val durationSeconds: Int?,
    val distanceMeters: Double?,
    val completionState: ExerciseSetCompletionState
)
