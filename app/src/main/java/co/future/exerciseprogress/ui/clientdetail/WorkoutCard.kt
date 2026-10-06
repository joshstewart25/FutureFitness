package co.future.exerciseprogress.ui.clientdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.components.FallingConfetti
import co.future.exerciseprogress.utils.extensions.toDayHeading
import co.future.exerciseprogress.utils.formatMinutes

@Composable
fun WorkoutCard(
    workout: WorkoutCardUiState,
    modifier: Modifier = Modifier
) {
    val dayLabel = if (workout.isToday) {
        stringResource(R.string.workout_today)
    } else {
        workout.date.toDayHeading()
    }
    val title = if (workout.isRestDay) {
        stringResource(R.string.workout_recovery_day)
    } else {
        workout.name
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Box {
            if (workout.isRestDay) {
                FallingConfetti(modifier = Modifier.matchParentSize())
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = dayLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (workout.description.isNotBlank()) {
                    Text(
                        text = workout.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (workout.durationMinutes != null) {
                    Text(
                        text = formatMinutes(workout.durationMinutes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
