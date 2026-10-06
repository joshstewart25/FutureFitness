package co.future.exerciseprogress.data.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WorkoutCompletionState {
    @SerialName("none")
    NONE,
    @SerialName("partial")
    PARTIAL,
    @SerialName("full")
    FULL
}
