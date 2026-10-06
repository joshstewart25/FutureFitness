package co.future.exerciseprogress.ui.previousworkouts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.theme.MissedCardDark
import co.future.exerciseprogress.ui.theme.MissedCardLight
import co.future.exerciseprogress.ui.theme.NotCompletedCardDark
import co.future.exerciseprogress.ui.theme.NotCompletedCardLight
import co.future.exerciseprogress.utils.extensions.toDayHeadingWithYear

private const val DESCRIPTION_MAX_LINES = 2

@Composable
fun PreviousWorkoutCard(
    workout: PreviousWorkoutCardUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = if (workout.isRestDay) {
        stringResource(R.string.workout_recovery_day)
    } else {
        workout.name
    }

    Card(
        onClick = onClick,
        colors = workout.status.cardColors(),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // The status text backs up the card color so the meaning doesn't rely on color alone.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = workout.finishedDate.toDayHeadingWithYear(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(workout.status.labelRes),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (workout.description.isNotBlank()) {
                Text(
                    text = workout.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = DESCRIPTION_MAX_LINES,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Completed workouts keep the normal card color.
@Composable
private fun PreviousWorkoutStatus.cardColors(): CardColors {
    val isDarkTheme = isSystemInDarkTheme()

    return when (this) {
        PreviousWorkoutStatus.COMPLETED -> CardDefaults.cardColors()
        PreviousWorkoutStatus.NOT_COMPLETED -> CardDefaults.cardColors(
            containerColor = if (isDarkTheme) NotCompletedCardDark else NotCompletedCardLight
        )
        PreviousWorkoutStatus.MISSED -> CardDefaults.cardColors(
            containerColor = if (isDarkTheme) MissedCardDark else MissedCardLight
        )
    }
}
