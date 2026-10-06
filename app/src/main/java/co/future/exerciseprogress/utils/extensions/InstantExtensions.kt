package co.future.exerciseprogress.utils.extensions

import android.icu.text.MessageFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

// Example output: "December 1st, 2020"
// The month name and the "1st" ending follow the device's locale, but the order is English only.
fun Instant.toLongDateString(): String {
    val date = toJavaInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val monthName = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    return "$monthName ${date.dayOfMonth.withOrdinalSuffix()}, ${date.year}"
}

fun Instant.toLocalDate(zone: ZoneId): LocalDate {
    return toJavaInstant().atZone(zone).toLocalDate()
}

// The backend sends 0001-01-01 instead of null for dates that were never set.
private val UNSET_INSTANT = Instant.parse("0001-01-01T00:00:00Z")

// Returns null when the date is missing or is the backend's "never set" value.
fun Instant?.takeIfSet(): Instant? {
    return this?.takeIf { it != UNSET_INSTANT }
}

// 1 -> "1st", 2 -> "2nd", 11 -> "11th" in English. Other languages use their own ordinal style.
private fun Int.withOrdinalSuffix(): String {
    return MessageFormat.format("{0,ordinal}", this)
}
