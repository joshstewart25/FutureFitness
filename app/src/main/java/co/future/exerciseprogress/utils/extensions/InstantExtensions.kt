package co.future.exerciseprogress.utils.extensions

import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

// Example output: "December 1st, 2020"
// The month name follows the device's locale, but the "1st" ending and the order are English only.
fun Instant.toLongDateString(): String {
    val date = toJavaInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val monthName = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return "$monthName ${date.dayOfMonth.withOrdinalSuffix()}, ${date.year}"
}

// 1 -> "1st", 2 -> "2nd", 3 -> "3rd", 4 -> "4th". The teens (11, 12, 13) are the exception and use "th".
private fun Int.withOrdinalSuffix(): String {
    val suffix = when {
        this % 100 in 11..13 -> "th"
        this % 10 == 1 -> "st"
        this % 10 == 2 -> "nd"
        this % 10 == 3 -> "rd"
        else -> "th"
    }
    return "$this$suffix"
}
