package co.future.exerciseprogress.ui.previousworkouts

import androidx.annotation.StringRes
import co.future.exerciseprogress.R
import co.future.exerciseprogress.utils.DateRange
import java.time.LocalDate

enum class PreviousWorkoutStatus(@StringRes val labelRes: Int) {
    COMPLETED(R.string.workout_status_completed),
    NOT_COMPLETED(R.string.workout_status_not_completed),
    MISSED(R.string.workout_status_missed)
}

// An empty selection means "don't filter by this", so everything is shown.
data class PreviousWorkoutsFilters(
    val statuses: Set<PreviousWorkoutStatus> = emptySet(),
    val dateRange: DateRange? = null
) {
    val isActive: Boolean
        get() = statuses.isNotEmpty() || dateRange != null
}

data class PreviousWorkoutsUiState(
    // Already filtered and sorted, most recently finished first.
    val workouts: List<PreviousWorkoutCardUiState> = emptyList(),
    val filters: PreviousWorkoutsFilters = PreviousWorkoutsFilters(),
    // Lets the screen tell "this client has no history" apart from "the filters hid everything".
    val hasAnyWorkouts: Boolean = false,
    // The month the date range picker opens on, so users don't have to scroll to the data.
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
