package co.future.exerciseprogress.ui.workoutdetail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.future.exerciseprogress.R
import co.future.exerciseprogress.data.models.enums.ExerciseSetCompletionState
import java.text.DecimalFormat
import kotlin.math.roundToInt

private const val RESULT_PART_SEPARATOR = " · "
private val SET_NUMBER_WIDTH = 56.dp

@Composable
fun SetSectionCard(
    section: SetSectionUiState,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toggleLabel = if (isExpanded) {
        stringResource(R.string.set_section_collapse, section.name)
    } else {
        stringResource(R.string.set_section_expand, section.name)
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.animateContentSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClickLabel = toggleLabel, onClick = onToggle)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pluralStringResource(
                            R.plurals.set_section_progress,
                            section.totalSetCount,
                            section.completedSetCount,
                            section.totalSetCount
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Filled.ExpandLess
                    } else {
                        Icons.Filled.ExpandMore
                    },
                    contentDescription = null
                )
            }

            if (isExpanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    section.exercises.forEach { exercise ->
                        ExerciseSets(exercise = exercise)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseSets(exercise: ExerciseUiState) {
    Column {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        exercise.sets.forEachIndexed { index, set ->
            SetRow(setNumber = index + 1, set = set)
        }
    }
}

@Composable
private fun SetRow(
    setNumber: Int,
    set: SetResultUiState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Text(
            text = stringResource(R.string.set_number, setNumber),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(SET_NUMBER_WIDTH)
        )
        Text(
            text = set.resultText(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        CompletionIcon(state = set.completionState)
    }
}

@Composable
private fun CompletionIcon(state: ExerciseSetCompletionState) {
    val icon = when (state) {
        ExerciseSetCompletionState.FULL -> Icons.Filled.CheckCircle
        ExerciseSetCompletionState.PARTIAL -> Icons.Filled.RemoveCircleOutline
        ExerciseSetCompletionState.NONE -> Icons.Filled.Cancel
    }
    val tint = when (state) {
        ExerciseSetCompletionState.FULL -> MaterialTheme.colorScheme.primary
        ExerciseSetCompletionState.PARTIAL -> MaterialTheme.colorScheme.tertiary
        ExerciseSetCompletionState.NONE -> MaterialTheme.colorScheme.error
    }
    val description = when (state) {
        ExerciseSetCompletionState.FULL -> stringResource(R.string.set_state_completed)
        ExerciseSetCompletionState.PARTIAL -> stringResource(R.string.set_state_partial)
        ExerciseSetCompletionState.NONE -> stringResource(R.string.set_state_skipped)
    }

    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}

// Example output: "300 lbs · 3 reps" or "30 sec"
@Composable
private fun SetResultUiState.resultText(): String {
    val parts = listOfNotNull(
        weight?.let { "${formatWeight(it)} $weightUnit" },
        reps?.let { pluralStringResource(R.plurals.set_result_reps, it, it) },
        durationSeconds?.let { stringResource(R.string.set_result_seconds, it) },
        distanceMeters?.let { stringResource(R.string.set_result_meters, it.roundToInt()) }
    )
    return parts.joinToString(RESULT_PART_SEPARATOR)
}

private fun formatWeight(weight: Float): String {
    return DecimalFormat("#.##").format(weight)
}
