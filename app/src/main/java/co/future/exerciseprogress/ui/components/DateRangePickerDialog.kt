package co.future.exerciseprogress.ui.components

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import co.future.exerciseprogress.R
import co.future.exerciseprogress.utils.DateRange
import co.future.exerciseprogress.utils.extensions.pickerMillisToLocalDate
import co.future.exerciseprogress.utils.extensions.toPickerMillis
import java.time.LocalDate

// Confirming with nothing selected reports null, which clears the range.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    initialDateRange: DateRange?,
    // The month shown when no range is selected yet.
    initialDisplayedDate: LocalDate,
    onConfirm: (DateRange?) -> Unit,
    onDismiss: () -> Unit
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialDateRange?.start?.toPickerMillis(),
        initialSelectedEndDateMillis = initialDateRange?.end?.toPickerMillis(),
        initialDisplayedMonthMillis = (initialDateRange?.start ?: initialDisplayedDate).toPickerMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val start = pickerState.selectedStartDateMillis?.pickerMillisToLocalDate()
                    // Picking a single day selects only a start date, so that day is also the end.
                    val end = pickerState.selectedEndDateMillis?.pickerMillisToLocalDate() ?: start
                    onConfirm(if (start != null && end != null) DateRange(start, end) else null)
                }
            ) {
                Text(text = stringResource(R.string.date_range_picker_apply))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.date_range_picker_cancel))
            }
        }
    ) {
        DateRangePicker(
            state = pickerState,
            // The calendar already shows the year. Leaving it out of the heading keeps it on one line.
            dateFormatter = remember { DatePickerDefaults.dateFormatter(selectedDateSkeleton = "MMMd") },
            title = null
        )
    }
}
