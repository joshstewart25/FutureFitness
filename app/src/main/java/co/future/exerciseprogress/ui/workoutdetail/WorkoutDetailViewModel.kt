package co.future.exerciseprogress.ui.workoutdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import co.future.exerciseprogress.data.WorkoutsRepository
import co.future.exerciseprogress.data.models.ExerciseSet
import co.future.exerciseprogress.data.models.ExerciseSetSummary
import co.future.exerciseprogress.data.models.HeartRateSample
import co.future.exerciseprogress.data.models.Workout
import co.future.exerciseprogress.data.models.WorkoutSummary
import co.future.exerciseprogress.data.models.enums.ExerciseSetCompletionState
import co.future.exerciseprogress.data.models.enums.ExerciseSetType
import co.future.exerciseprogress.data.models.enums.WorkoutCompletionState
import co.future.exerciseprogress.ui.navigation.WorkoutDetail
import co.future.exerciseprogress.ui.workoutdetail.enums.DifficultyLevel
import co.future.exerciseprogress.utils.extensions.takeIfSet
import co.future.exerciseprogress.utils.extensions.toggled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.Instant

private const val EASY_CUTOFF = 1f / 3f
private const val MODERATE_CUTOFF = 2f / 3f
private const val MIN_HEART_RATE_POINTS = 2

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    workoutsRepository: WorkoutsRepository
) : ViewModel() {

    private val workoutID = savedStateHandle.toRoute<WorkoutDetail>().workoutID

    private val _uiState = MutableStateFlow(
        buildUiState(workout = workoutsRepository.workouts.firstOrNull { it.id == workoutID })
    )
    val uiState: StateFlow<WorkoutDetailUiState> = _uiState.asStateFlow()

    fun onSectionToggled(sectionID: String) {
        _uiState.update { it.copy(expandedSectionIDs = it.expandedSectionIDs.toggled(sectionID)) }
    }
}

private fun buildUiState(workout: Workout?): WorkoutDetailUiState {
    if (workout == null) {
        return WorkoutDetailUiState(
            name = "",
            description = "",
            isRestDay = false,
            summary = null
        )
    }

    val displayedSummary = workout.displayedSummary()

    return WorkoutDetailUiState(
        name = workout.name.orEmpty(),
        description = workout.description.orEmpty(),
        isRestDay = workout.isRestDay,
        summary = displayedSummary?.toUiState(workout)
    )
}

// The attempt the dashboard describes: the most recent completed one, or else the most recent one that was started.
// A summary with no date counts as the oldest.
private fun Workout.displayedSummary(): WorkoutSummary? {
    val completedSummaries = summaries.filter { it.completionState == WorkoutCompletionState.FULL }
    val latestCompleted = completedSummaries.maxByOrNull { it.completedAt ?: Instant.DISTANT_PAST }
    if (latestCompleted != null) {
        return latestCompleted
    }

    return summaries.maxByOrNull { it.startedAt.takeIfSet() ?: Instant.DISTANT_PAST }
}

private fun WorkoutSummary.toUiState(workout: Workout): WorkoutSummaryUiState {
    return WorkoutSummaryUiState(
        isCompleted = completionState == WorkoutCompletionState.FULL,
        heartRateChart = buildHeartRateChart(heartRates),
        averageHeartRate = averageHeartRate,
        maxHeartRate = maxHeartRate,
        activeEnergyBurned = activeEnergyBurned,
        durationMinutes = actualDuration
            ?.let { it.seconds.toDouble(DurationUnit.MINUTES).roundToInt() }
            ?.takeIf { it > 0 },
        notes = notes?.trim()?.takeIf { it.isNotEmpty() },
        difficulty = difficulty?.toDifficultyUiState(),
        location = locations.firstOrNull()?.let { LocationUiState(it.latitude, it.longitude) },
        sections = buildSetSections(workout, summary = this)
    )
}

// Time on the chart counts from the first reading, so it lines up with the readings rather than the clock.
private fun buildHeartRateChart(samples: List<HeartRateSample>): HeartRateChartUiState? {
    val sortedSamples = samples.sortedBy { it.recordedAt }
    val firstReadingAt = sortedSamples.firstOrNull()?.recordedAt ?: return null

    // Readings in the same second would stack on top of each other, so only the first one is kept.
    val points = sortedSamples
        .map { HeartRatePoint((it.recordedAt - firstReadingAt).inWholeSeconds.toInt(), it.value) }
        .distinctBy { it.secondsSinceStart }
    if (points.size < MIN_HEART_RATE_POINTS) return null

    return HeartRateChartUiState(
        points = points,
        peak = points.maxBy { it.bpm },
        lowestBpm = points.minOf { it.bpm }
    )
}

private fun Float.toDifficultyUiState(): DifficultyUiState {
    val level = when {
        this < EASY_CUTOFF -> DifficultyLevel.EASY
        this < MODERATE_CUTOFF -> DifficultyLevel.MODERATE
        else -> DifficultyLevel.HARD
    }
    return DifficultyUiState(value = coerceIn(0f, 1f), level = level)
}

private data class PerformedSet(
    val set: ExerciseSet,
    val summary: ExerciseSetSummary
)

// Follows the workout's own order. Rest sets are skipped, and sets that were never reached have no summary so they are
// left out too. Sets of the same exercise are grouped together, even when the workout alternates between exercises.
private fun buildSetSections(workout: Workout, summary: WorkoutSummary): List<SetSectionUiState> {
    return workout.sections.mapNotNull { section ->
        val performedSets = section.exerciseSets
            .filterNot { it.isRecovery }
            .sortedBy { it.position }
            .flatMap { set -> summary.setSummariesFor(set.id).map { PerformedSet(set, it) } }
        if (performedSets.isEmpty()) return@mapNotNull null

        SetSectionUiState(
            id = section.id,
            name = section.name.orEmpty(),
            completedSetCount = performedSets.count { it.summary.completionState == ExerciseSetCompletionState.FULL },
            totalSetCount = performedSets.size,
            exercises = performedSets
                .groupBy { it.set.exercise?.id }
                .values
                .map { exerciseSets ->
                    ExerciseUiState(
                        name = exerciseSets.first().set.exercise?.name.orEmpty(),
                        sets = exerciseSets.map { it.toUiState() }
                    )
                }
        )
    }
}

private fun PerformedSet.toUiState(): SetResultUiState {
    return SetResultUiState(
        weight = (summary.weight ?: set.weight)?.takeIf { it > 0f },
        weightUnit = set.weightUnit,
        reps = if (set.type == ExerciseSetType.REPS) {
            summary.repsReported ?: summary.repsCounted ?: set.reps
        } else {
            null
        },
        durationSeconds = if (set.type == ExerciseSetType.DURATION) {
            summary.timeSpentActive ?: set.duration
        } else {
            null
        },
        distanceMeters = if (set.type == ExerciseSetType.DISTANCE) {
            set.distance
        } else {
            null
        },
        completionState = summary.completionState
    )
}
