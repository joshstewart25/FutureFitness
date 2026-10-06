package co.future.exerciseprogress.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Welcome

@Serializable
data class ClientDetail(val clientID: String)

@Serializable
data class PreviousWorkouts(val clientID: String)
