package co.future.exerciseprogress.data.models

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
        get() = if (isTwoDumbbells == true) "lb dumbbells" else "lbs"
}

enum class Intensity(val rawValue: String) {
    NONE(""),
    TEMPO_FAST("tempo_fast"),
    TEMPO_MODERATE("tempo_moderate"),
    TEMPO_SLOW("tempo_slow"),
    PACE_FAST("pace_fast"),
    PACE_MODERATE("pace_moderate"),
    PACE_SLOW("pace_slow"),
    EFFORT_MAX("effort_max"),
    PACE_MAX("pace_max"),
    AMRAP("amrap"),
    REPS_FAST("reps_fast"),
    REPS_SLOW("reps_slow"),
    RESISTANCE_LOW("resistance_low"),
    RESISTANCE_MEDIUM("resistance_medium"),
    RESISTANCE_HIGH("resistance_high"),
    SPEED_RPM_UNDER_65("speed_rpm_under_65"),
    SPEED_RPM_65_TO_80("speed_rpm_65_80"),
    SPEED_RPM_80_TO_90("speed_rpm_80_90"),
    SPEED_RPM_OVER_110("speed_rpm_over_110"),
    LIGHT("light"),
    SLOW("slow"),
    WEIGHT_HEAVY("weight_heavy"),
    WEIGHT_LIGHT("weight_light"),
    WEIGHT_MODERATE("weight_moderate");
    
    val displayName: String
        get() = when (this) {
            NONE -> "Normal"
            TEMPO_FAST -> "Fast Tempo"
            TEMPO_MODERATE -> "Moderate Tempo"
            TEMPO_SLOW -> "Slow Tempo"
            PACE_FAST -> "Fast Pace"
            PACE_MODERATE -> "Moderate Pace"
            PACE_SLOW -> "Slow Pace"
            EFFORT_MAX -> "Max Effort"
            PACE_MAX -> "Max Pace"
            AMRAP -> "AMRAP"
            REPS_FAST -> "Fast Reps"
            REPS_SLOW -> "Slow Reps"
            LIGHT -> "Light"
            SLOW -> "Slow"
            WEIGHT_HEAVY -> "Heavy"
            WEIGHT_LIGHT -> "Light"
            WEIGHT_MODERATE -> "Moderate"
            RESISTANCE_LOW -> "Low Resistance"
            RESISTANCE_MEDIUM -> "Medium Resistance"
            RESISTANCE_HIGH -> "High Resistance"
            SPEED_RPM_UNDER_65 -> "Under 65 RPM"
            SPEED_RPM_65_TO_80 -> "65 to 80 RPM"
            SPEED_RPM_80_TO_90 -> "80 to 90 RPM"
            SPEED_RPM_OVER_110 -> "Over 100 RPM"
        }
    
    companion object {
        fun fromRawValue(value: String): Intensity? {
            return entries.find { it.rawValue == value }
        }
    }
}