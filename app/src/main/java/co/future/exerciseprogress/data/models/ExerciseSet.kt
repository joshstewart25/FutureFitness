package co.future.exerciseprogress.data.models

import co.future.exerciseprogress.data.models.enums.ExerciseSetType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSet(
    @SerialName("id")
    val id: String,
    
    @SerialName("exercise")
    val exercise: Exercise? = null,
    
    @SerialName("type")
    val type: ExerciseSetType = ExerciseSetType.UNKNOWN,
    
    @SerialName("reps")
    val reps: Int? = null,
    
    @SerialName("min_reps")
    val minReps: Int? = null,
    
    @SerialName("max_reps")
    val maxReps: Int? = null,
    
    @SerialName("duration")
    val duration: Int? = null,
    
    @SerialName("weight")
    val weight: Float? = null,
    
    @SerialName("position")
    val position: Int? = null,
    
    @SerialName("distance")
    val distance: Double? = null,
    
    @SerialName("estimated_duration")
    val estimatedDuration: Int? = null,
    
    @SerialName("start_timing")
    val startTiming: Double = 6.0,
    
    @SerialName("intensity")
    val intensity: String? = null,
    
    @SerialName("unit")
    val unit: String? = null,
    
    @SerialName("is_two_dumbbells")
    val isTwoDumbbells: Boolean? = null
) {
    val isRecovery: Boolean
        get() = exercise?.type == "rest"
    
    val weightUnit: String
        get() = if (isTwoDumbbells == true) {
            "lb dumbbells"
        } else {
            "lbs"
        }
}
