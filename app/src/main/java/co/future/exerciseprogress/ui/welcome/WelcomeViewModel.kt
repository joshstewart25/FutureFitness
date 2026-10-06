package co.future.exerciseprogress.ui.welcome

import androidx.lifecycle.ViewModel
import co.future.exerciseprogress.data.ClientsRepository
import co.future.exerciseprogress.data.WorkoutsRepository
import co.future.exerciseprogress.data.models.Client
import co.future.exerciseprogress.data.models.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    clientsRepository: ClientsRepository,
    workoutsRepository: WorkoutsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WelcomeUiState(
            clients = buildClientCards(
                clients = clientsRepository.clients,
                workouts = workoutsRepository.workouts
            )
        )
    )
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()
}

// Most recent workout first. Clients with no completed workouts go last, in alphabetical order.
private fun buildClientCards(
    clients: List<Client>,
    workouts: List<Workout>
): List<ClientCardUiState> {
    val workoutsByUserID = workouts.groupBy { it.userID }

    val clientCards = clients.map { client ->
        val clientWorkouts = workoutsByUserID[client.id].orEmpty()
        ClientCardUiState(
            id = client.id,
            name = client.fullName,
            lastWorkoutCompletedAt = clientWorkouts.mapNotNull { it.lastCompletedAt }.maxOrNull()
        )
    }

    val (clientsWithWorkouts, clientsWithoutWorkouts) = clientCards.partition {
        it.lastWorkoutCompletedAt != null
    }

    return clientsWithWorkouts.sortedByDescending { it.lastWorkoutCompletedAt } +
        clientsWithoutWorkouts.sortedBy { it.name }
}
