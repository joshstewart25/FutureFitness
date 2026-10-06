package co.future.exerciseprogress.ui.previousworkouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.components.DateRangePickerDialog
import co.future.exerciseprogress.utils.DateRange
import co.future.exerciseprogress.utils.extensions.toMediumDate
import java.time.LocalDate

@Composable
fun PreviousWorkoutsFilterBar(
    filters: PreviousWorkoutsFilters,
    datePickerStartDate: LocalDate,
    onStatusToggled: (PreviousWorkoutStatus) -> Unit,
    onDateRangeSelected: (DateRange?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDatePickerShown by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PreviousWorkoutStatus.entries.forEach { status ->
                FilterChip(
                    selected = status in filters.statuses,
                    onClick = { onStatusToggled(status) },
                    label = { Text(text = stringResource(status.labelRes)) }
                )
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = filters.dateRange != null,
                onClick = { isDatePickerShown = true },
                label = { Text(text = filters.dateRange.toLabel()) }
            )

            if (filters.isActive) {
                TextButton(onClick = onClearFilters) {
                    Text(text = stringResource(R.string.filter_clear))
                }
            }
        }
    }

    if (isDatePickerShown) {
        DateRangePickerDialog(
            initialDateRange = filters.dateRange,
            initialDisplayedDate = datePickerStartDate,
            onConfirm = { dateRange ->
                onDateRangeSelected(dateRange)
                isDatePickerShown = false
            },
            onDismiss = { isDatePickerShown = false }
        )
    }
}

@Composable
private fun DateRange?.toLabel(): String {
    return if (this == null) {
        stringResource(R.string.filter_date_any)
    } else {
        stringResource(R.string.filter_date_range, start.toMediumDate(), end.toMediumDate())
    }
}
