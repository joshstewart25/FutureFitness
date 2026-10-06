package co.future.exerciseprogress.utils.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

// Example output: "Tuesday, December 1". The weekday and month names follow the device's locale.
fun LocalDate.toDayHeading(): String {
    return format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault()))
}

// Example output: "Tuesday, December 1, 2020". Used for history, where the year matters.
fun LocalDate.toDayHeadingWithYear(): String {
    return format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.getDefault()))
}

// Example output: "Dec 1, 2020". The order and month name follow the device's locale.
fun LocalDate.toMediumDate(): String {
    return format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
}

// Material date pickers read and report dates as midnight UTC in milliseconds.
fun LocalDate.toPickerMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun Long.pickerMillisToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}
