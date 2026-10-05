package co.future.exerciseprogress.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSection(
    @SerialName("id")
    val id: String,
    
    @SerialName("name")
    val name: String? = null,
    
    @SerialName("type")
    val type: String? = null,
    
    @SerialName("workout_id")
    val workoutID: String? = null,
    
    @SerialName("sets")
    val exerciseSets: List<ExerciseSet> = emptyList(),
    
    @SerialName("skip_transitions")
    val skipTransitions: Boolean = false
)