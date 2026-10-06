package co.future.exerciseprogress.utils

import java.time.LocalDate

// Both the start and end dates are part of the range.
data class DateRange(
    val start: LocalDate,
    val end: LocalDate
) {
    operator fun contains(date: LocalDate): Boolean {
        return date in start..end
    }
}
