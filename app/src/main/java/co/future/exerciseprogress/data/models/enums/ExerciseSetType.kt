package co.future.exerciseprogress.data.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ExerciseSetType {
    @SerialName("")
    UNKNOWN,
    @SerialName("reps")
    REPS,
    @SerialName("duration")
    DURATION,
    @SerialName("distance")
    DISTANCE
}
