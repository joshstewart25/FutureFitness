package co.future.exerciseprogress.ui.clientdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import co.future.exerciseprogress.data.ClientsRepository
import co.future.exerciseprogress.data.WorkoutsRepository
import co.future.exerciseprogress.data.models.Workout
import co.future.exerciseprogress.ui.navigation.ClientDetail
import co.future.exerciseprogress.utils.extensions.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

// Today plus the next six days.
private const val UPCOMING_DAYS = 7

private const val SECONDS_PER_MINUTE = 60

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    clientsRepository: ClientsRepository,
    workoutsRepository: WorkoutsRepository,
    clock: Clock
) : ViewModel() {

    private val clientID = savedStateHandle.toRoute<ClientDetail>().clientID

    private val _uiState = MutableStateFlow(
        ClientDetailUiState(
            clientName = clientsRepository.getClient(clientID)?.fullName.orEmpty(),
            upcomingWorkouts = buildUpcomingWorkouts(
                workouts = workoutsRepository.workouts.filter { it.userID == clientID },
                clock = clock
            )
        )
    )
    val uiState: StateFlow<ClientDetailUiState> = _uiState.asStateFlow()
}

// Workouts scheduled from today through the next six days, earliest first.
// Workouts with no scheduled date are left out.
private fun buildUpcomingWorkouts(
    workouts: List<Workout>,
    clock: Clock
): List<WorkoutCardUiState> {
    val today = LocalDate.now(clock)
    val lastDay = today.plusDays(UPCOMING_DAYS - 1L)

    return workouts
        .sortedBy { it.scheduledAt }
        .mapNotNull { workout ->
            val date = workout.scheduledAt?.toLocalDate(clock.zone) ?: return@mapNotNull null
            if (date !in today..lastDay) return@mapNotNull null

            WorkoutCardUiState(
                id = workout.id,
                date = date,
                isToday = date == today,
                isRestDay = workout.isRestDay,
                name = workout.name.orEmpty(),
                description = workout.description.orEmpty(),
                durationMinutes = workout.durationInMinutes()
            )
        }
}

// Workout durations are stored in seconds. Anything that rounds to zero minutes is treated as no duration.
private fun Workout.durationInMinutes(): Int? {
    return duration
        ?.let { (it / SECONDS_PER_MINUTE).roundToInt() }
        ?.takeIf { it > 0 }
}
