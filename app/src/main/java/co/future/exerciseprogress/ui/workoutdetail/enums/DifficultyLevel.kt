package co.future.exerciseprogress.ui.workoutdetail.enums

import androidx.annotation.StringRes
import co.future.exerciseprogress.R

enum class DifficultyLevel(@StringRes val labelRes: Int) {
    EASY(R.string.difficulty_easy),
    MODERATE(R.string.difficulty_moderate),
    HARD(R.string.difficulty_hard)
}
