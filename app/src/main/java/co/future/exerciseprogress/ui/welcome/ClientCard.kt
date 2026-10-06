package co.future.exerciseprogress.ui.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.components.DefaultProfilePicture
import co.future.exerciseprogress.utils.extensions.toLongDateString

@Composable
fun ClientCard(
    client: ClientCardUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lastWorkoutCompletedAt = client.lastWorkoutCompletedAt
    val lastWorkoutText = if (lastWorkoutCompletedAt != null) {
        stringResource(R.string.client_last_workout, lastWorkoutCompletedAt.toLongDateString())
    } else {
        stringResource(R.string.client_no_workouts)
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultProfilePicture()
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lastWorkoutText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
