package co.future.exerciseprogress.ui.workoutdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
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
import co.future.exerciseprogress.ui.theme.HeartRateRed
import co.future.exerciseprogress.utils.formatMinutes

@Composable
fun WorkoutDetailScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkoutDetailContent(
        uiState = uiState,
        contentPadding = contentPadding,
        onSectionToggled = viewModel::onSectionToggled,
        modifier = modifier
    )
}

@Composable
private fun WorkoutDetailContent(
    uiState: WorkoutDetailUiState,
    contentPadding: PaddingValues,
    onSectionToggled: (sectionID: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val summary = uiState.summary

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding.withExtraPadding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            WorkoutHeader(
                uiState = uiState,
                isNotCompleted = summary != null && !summary.isCompleted
            )
        }

        if (summary == null) {
            item {
                Text(
                    text = stringResource(R.string.workout_detail_no_data),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            summaryItems(
                summary = summary,
                expandedSectionIDs = uiState.expandedSectionIDs,
                onSectionToggled = onSectionToggled
            )
        }
    }
}

@Composable
private fun WorkoutHeader(
    uiState: WorkoutDetailUiState,
    isNotCompleted: Boolean
) {
    val title = if (uiState.isRestDay) {
        stringResource(R.string.workout_recovery_day)
    } else {
        uiState.name
    }

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        if (isNotCompleted) {
            Text(
                text = stringResource(R.string.workout_status_not_completed),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        if (uiState.description.isNotBlank()) {
            Text(
                text = uiState.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Each part of the dashboard is only added when the workout has data for it.
private fun LazyListScope.summaryItems(
    summary: WorkoutSummaryUiState,
    expandedSectionIDs: Set<String>,
    onSectionToggled: (sectionID: String) -> Unit
) {
    summary.heartRateChart?.let { chart ->
        item { HeartRateChartCard(chart = chart) }
    }

    if (summary.maxHeartRate != null || summary.averageHeartRate != null) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                summary.maxHeartRate?.let { maxHeartRate ->
                    StatTile(
                        icon = Icons.Filled.Favorite,
                        iconTint = HeartRateRed,
                        label = stringResource(R.string.heart_rate_max),
                        value = stringResource(R.string.heart_rate_value, maxHeartRate),
                        modifier = Modifier.weight(1f)
                    )
                }
                summary.averageHeartRate?.let { averageHeartRate ->
                    StatTile(
                        icon = Icons.Filled.FavoriteBorder,
                        iconTint = HeartRateRed,
                        label = stringResource(R.string.heart_rate_average),
                        value = stringResource(R.string.heart_rate_value, averageHeartRate),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    if (summary.activeEnergyBurned != null || summary.durationMinutes != null) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                summary.activeEnergyBurned?.let { energyBurned ->
                    StatTile(
                        icon = Icons.Filled.LocalFireDepartment,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        label = stringResource(R.string.energy_burned),
                        value = stringResource(R.string.energy_burned_value, energyBurned),
                        modifier = Modifier.weight(1f)
                    )
                }
                summary.durationMinutes?.let { durationMinutes ->
                    StatTile(
                        icon = Icons.Filled.Timer,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        label = stringResource(R.string.workout_duration_label),
                        value = formatMinutes(durationMinutes),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    summary.notes?.let { notes ->
        item { NotesCard(notes = notes) }
    }

    summary.difficulty?.let { difficulty ->
        item { DifficultyCard(difficulty = difficulty) }
    }

    summary.location?.let { location ->
        item { LocationCard(location = location) }
    }

    if (summary.sections.isNotEmpty()) {
        item {
            Text(
                text = stringResource(R.string.set_summaries),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        items(summary.sections, key = { it.id }) { section ->
            SetSectionCard(
                section = section,
                isExpanded = section.id in expandedSectionIDs,
                onToggle = { onSectionToggled(section.id) }
            )
        }
    }
}
