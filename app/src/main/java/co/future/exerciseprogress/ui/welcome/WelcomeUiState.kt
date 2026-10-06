package co.future.exerciseprogress.ui.welcome

import kotlin.time.Instant

data class WelcomeUiState(
    val clients: List<ClientCardUiState> = emptyList()
)

data class ClientCardUiState(
    val id: String,
    val name: String,
    val lastWorkoutCompletedAt: Instant?
)
