package co.future.exerciseprogress.ui.clientdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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

@Composable
fun ClientDetailScreen(
    contentPadding: PaddingValues,
    onPreviousWorkoutsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClientDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClientDetailContent(
        uiState = uiState,
        contentPadding = contentPadding,
        onPreviousWorkoutsClick = onPreviousWorkoutsClick,
        modifier = modifier
    )
}

@Composable
private fun ClientDetailContent(
    uiState: ClientDetailUiState,
    contentPadding: PaddingValues,
    onPreviousWorkoutsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding.withExtraPadding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = uiState.clientName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (uiState.upcomingWorkouts.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.upcoming_workouts_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(uiState.upcomingWorkouts, key = { it.id }) { workout ->
                WorkoutCard(workout = workout)
            }
        }

        item {
            Button(
                onClick = onPreviousWorkoutsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.previous_workouts))
            }
        }
    }
}
