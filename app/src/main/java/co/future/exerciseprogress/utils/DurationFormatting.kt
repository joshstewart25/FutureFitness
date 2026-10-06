package co.future.exerciseprogress.utils

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.util.Locale
import kotlin.time.Duration.Companion.minutes

// Example output: "58 min" or "1 hr, 8 min". The wording follows the device's locale.
fun formatMinutes(totalMinutes: Int): String {
    val measureFormat = MeasureFormat.getInstance(Locale.getDefault(), MeasureFormat.FormatWidth.SHORT)

    return totalMinutes.minutes.toComponents { hours, minutes, _, _ ->
        if (hours > 0) {
            measureFormat.formatMeasures(
                Measure(hours, MeasureUnit.HOUR),
                Measure(minutes, MeasureUnit.MINUTE)
            )
        } else {
            measureFormat.formatMeasures(Measure(minutes, MeasureUnit.MINUTE))
        }
    }
}
