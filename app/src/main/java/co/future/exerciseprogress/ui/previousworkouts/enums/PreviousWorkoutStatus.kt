package co.future.exerciseprogress.ui.previousworkouts.enums

import androidx.annotation.StringRes
import co.future.exerciseprogress.R

enum class PreviousWorkoutStatus(@StringRes val labelRes: Int) {
    COMPLETED(R.string.workout_status_completed),
    NOT_COMPLETED(R.string.workout_status_not_completed),
    MISSED(R.string.workout_status_missed)
}
