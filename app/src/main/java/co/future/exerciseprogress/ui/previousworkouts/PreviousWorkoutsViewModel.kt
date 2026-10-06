package co.future.exerciseprogress.ui.previousworkouts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import co.future.exerciseprogress.data.WorkoutsRepository
import co.future.exerciseprogress.data.models.Workout
import co.future.exerciseprogress.ui.navigation.PreviousWorkouts
import co.future.exerciseprogress.ui.previousworkouts.enums.PreviousWorkoutStatus
import co.future.exerciseprogress.utils.DateRange
import co.future.exerciseprogress.utils.extensions.takeIfSet
import co.future.exerciseprogress.utils.extensions.toLocalDate
import co.future.exerciseprogress.utils.extensions.toggled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Instant

@HiltViewModel
class PreviousWorkoutsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    workoutsRepository: WorkoutsRepository,
    clock: Clock
) : ViewModel() {

    private val clientID = savedStateHandle.toRoute<PreviousWorkouts>().clientID

    private val allWorkouts = buildPreviousWorkouts(
        workouts = workoutsRepository.workouts.filter { it.userID == clientID },
        zone = clock.zone
    )

    private val datePickerStartDate = allWorkouts.firstOrNull()?.finishedDate ?: LocalDate.now(clock)

    private val _uiState = MutableStateFlow(buildUiState(PreviousWorkoutsFilters()))
    val uiState: StateFlow<PreviousWorkoutsUiState> = _uiState.asStateFlow()

    fun onStatusToggled(status: PreviousWorkoutStatus) {
        updateFilters { it.copy(statuses = it.statuses.toggled(status)) }
    }

    fun onDateRangeSelected(dateRange: DateRange?) {
        updateFilters { it.copy(dateRange = dateRange) }
    }

    fun onClearFilters() {
        updateFilters { PreviousWorkoutsFilters() }
    }

    private fun updateFilters(change: (PreviousWorkoutsFilters) -> PreviousWorkoutsFilters) {
        _uiState.update { buildUiState(change(it.filters)) }
    }

    private fun buildUiState(filters: PreviousWorkoutsFilters): PreviousWorkoutsUiState {
        return PreviousWorkoutsUiState(
            workouts = allWorkouts.filter { filters.matches(it) },
            filters = filters,
            hasAnyWorkouts = allWorkouts.isNotEmpty(),
            datePickerStartDate = datePickerStartDate
        )
    }
}

private fun PreviousWorkoutsFilters.matches(workout: PreviousWorkoutCardUiState): Boolean {
    val matchesStatus = statuses.isEmpty() || workout.status in statuses
    val matchesDate = dateRange == null || workout.finishedDate in dateRange

    return matchesStatus && matchesDate
}

private data class FinishedWorkout(
    val workout: Workout,
    val status: PreviousWorkoutStatus,
    val finishedAt: Instant
)

// Workouts that were completed, started but not completed, or missed, most recently finished first.
// Anything else (rest days, upcoming workouts) has not finished yet and is left out.
private fun buildPreviousWorkouts(
    workouts: List<Workout>,
    zone: ZoneId
): List<PreviousWorkoutCardUiState> {
    return workouts
        .mapNotNull { it.toFinishedWorkout() }
        .sortedByDescending { it.finishedAt }
        .map { finished ->
            val workout = finished.workout
            PreviousWorkoutCardUiState(
                id = workout.id,
                status = finished.status,
                finishedDate = finished.finishedAt.toLocalDate(zone),
                isRestDay = workout.isRestDay,
                name = workout.name.orEmpty(),
                description = workout.description.orEmpty()
            )
        }
}

// If a workout has more than one signal, completed wins over started, and started wins over missed.
// Each status is dated by the moment that best marks it as over:
// completed -> when it was completed, not completed -> when it was started, missed -> when it was marked missed.
private fun Workout.toFinishedWorkout(): FinishedWorkout? {
    val completedAt = lastCompletedAt
    if (completedAt != null) {
        return FinishedWorkout(this, PreviousWorkoutStatus.COMPLETED, completedAt)
    }

    val startedAt = summaries.mapNotNull { it.startedAt.takeIfSet() }.maxOrNull()
    if (startedAt != null) {
        return FinishedWorkout(this, PreviousWorkoutStatus.NOT_COMPLETED, startedAt)
    }

    val markedMissedAt = missedAt.takeIfSet()
    if (markedMissedAt != null) {
        return FinishedWorkout(this, PreviousWorkoutStatus.MISSED, markedMissedAt)
    }

    return null
}
