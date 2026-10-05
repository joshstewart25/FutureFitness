package co.future.exerciseprogress.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    @SerialName("id")
    val id: String,
    
    @SerialName("name")
    val name: String? = null,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("equipment_required")
    val equipmentRequired: String? = null,
    
    @SerialName("estimated_rep_duration")
    val estimatedRepDuration: Double? = null,
    
    @SerialName("is_alternating")
    val isAlternating: Boolean = false,
    
    @SerialName("is_distance")
    val isDistance: Boolean = false,
    
    @SerialName("is_duration")
    val isDuration: Boolean = false,
    
    @SerialName("is_enabled")
    val isEnabled: Boolean? = null,
    
    @SerialName("is_reps")
    val isReps: Boolean = false,
    
    @SerialName("is_trackable_distance")
    val isTrackableDistance: Boolean = false,
    
    @SerialName("is_two_dumbbells")
    val isTwoDumbbells: Boolean = false,
    
    @SerialName("is_weight")
    val isWeight: Boolean = false,
    
    @SerialName("movement_patterns")
    val movementPatterns: String? = null,
    
    @SerialName("muscle_groups")
    val muscleGroups: String? = null,
    
    @SerialName("pace")
    val pace: Float? = null,
    
    @SerialName("side")
    val side: String? = null,
    
    @SerialName("source")
    val source: String? = null,
    
    @SerialName("tags")
    val tags: String? = null,
    
    @SerialName("type")
    val type: String? = null
) {
    val sideDisplayName: String?
        get() = side?.let { Side.fromRawValue(it)?.displayName }
}

enum class Side(val rawValue: String) {
    RIGHT("right_side"),
    LEFT("left_side"),
    RIGHT_ARM("right_arm"),
    LEFT_ARM("left_arm"),
    RIGHT_LEG("right_leg"),
    LEFT_LEG("left_leg");
    
    val displayName: String
        get() = when (this) {
            LEFT -> "Left Side"
            RIGHT -> "Right Side"
            LEFT_ARM -> "Left Arm"
            RIGHT_ARM -> "Right Arm"
            LEFT_LEG -> "Left Leg"
            RIGHT_LEG -> "Right Leg"
        }
    
    companion object {
        fun fromRawValue(value: String): Side? {
            return entries.find { it.rawValue == value }
        }
    }
}