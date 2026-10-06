package co.future.exerciseprogress.data.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ExerciseSetCompletionState {
    @SerialName("none")
    NONE,
    @SerialName("partial")
    PARTIAL,
    @SerialName("full")
    FULL
}
