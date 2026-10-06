package co.future.exerciseprogress.utils.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Example output: "Tuesday, December 1". The weekday and month names follow the device's locale.
fun LocalDate.toDayHeading(): String {
    return format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault()))
}
