package co.future.exerciseprogress.ui.previousworkouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.components.withExtraPadding
import co.future.exerciseprogress.ui.previousworkouts.enums.PreviousWorkoutStatus
import co.future.exerciseprogress.utils.DateRange

@Composable
fun PreviousWorkoutsScreen(
    contentPadding: PaddingValues,
    onWorkoutClick: (workoutID: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PreviousWorkoutsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PreviousWorkoutsContent(
        uiState = uiState,
        contentPadding = contentPadding,
        onWorkoutClick = onWorkoutClick,
        onStatusToggled = viewModel::onStatusToggled,
        onDateRangeSelected = viewModel::onDateRangeSelected,
        onClearFilters = viewModel::onClearFilters,
        modifier = modifier
    )
}

@Composable
private fun PreviousWorkoutsContent(
    uiState: PreviousWorkoutsUiState,
    contentPadding: PaddingValues,
    onWorkoutClick: (workoutID: String) -> Unit,
    onStatusToggled: (PreviousWorkoutStatus) -> Unit,
    onDateRangeSelected: (DateRange?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding.withExtraPadding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.previous_workouts),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (uiState.hasAnyWorkouts) {
            item {
                PreviousWorkoutsFilterBar(
                    filters = uiState.filters,
                    datePickerStartDate = uiState.datePickerStartDate,
                    onStatusToggled = onStatusToggled,
                    onDateRangeSelected = onDateRangeSelected,
                    onClearFilters = onClearFilters
                )
            }
        }

        if (uiState.workouts.isEmpty()) {
            item {
                Text(
                    text = stringResource(
                        if (uiState.hasAnyWorkouts) {
                            R.string.previous_workouts_no_matches
                        } else {
                            R.string.previous_workouts_empty
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(uiState.workouts, key = { it.id }) { workout ->
                PreviousWorkoutCard(
                    workout = workout,
                    onClick = { onWorkoutClick(workout.id) }
                )
            }
        }
    }
}
