package co.future.exerciseprogress.ui.previousworkouts

import co.future.exerciseprogress.ui.previousworkouts.enums.PreviousWorkoutStatus
import co.future.exerciseprogress.utils.DateRange
import java.time.LocalDate

// An empty selection means "don't filter by this", so everything is shown.
data class PreviousWorkoutsFilters(
    val statuses: Set<PreviousWorkoutStatus> = emptySet(),
    val dateRange: DateRange? = null
) {
    val isActive: Boolean
        get() = statuses.isNotEmpty() || dateRange != null
}

data class PreviousWorkoutsUiState(
    val workouts: List<PreviousWorkoutCardUiState> = emptyList(),
    val filters: PreviousWorkoutsFilters = PreviousWorkoutsFilters(),
    val hasAnyWorkouts: Boolean = false,
    val datePickerStartDate: LocalDate
)

data class PreviousWorkoutCardUiState(
    val id: String,
    val status: PreviousWorkoutStatus,
    val finishedDate: LocalDate,
    val isRestDay: Boolean,
    val name: String,
    val description: String
)
