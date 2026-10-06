package co.future.exerciseprogress.ui.clientdetail

import java.time.LocalDate

data class ClientDetailUiState(
    val clientName: String = "",
    val upcomingWorkouts: List<WorkoutCardUiState> = emptyList()
)

data class WorkoutCardUiState(
    val id: String,
    val date: LocalDate,
    val isToday: Boolean,
    val isRestDay: Boolean,
    val name: String,
    val description: String,
    val durationMinutes: Int?
)
