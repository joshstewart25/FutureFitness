package co.future.exerciseprogress.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Workout(
    @SerialName("id")
    val id: String,
    
    @SerialName("name")
    val name: String? = null,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("type")
    val type: String? = null,
    
    @SerialName("activity_type")
    val activityType: String? = null,
    
    @SerialName("duration")
    val duration: Double? = null,
    
    @SerialName("sections")
    val sections: List<WorkoutSection> = emptyList(),
    
    @SerialName("scheduled_at")
    val scheduledAt: Instant? = null,
    
    @SerialName("missed_at")
    val missedAt: Instant? = null,
    
    @SerialName("created_at")
    val createdAt: Instant? = null,
    
    @SerialName("updated_at")
    val updatedAt: Instant? = null,
    
    @SerialName("summaries")
    val summaries: List<WorkoutSummary> = emptyList()
) {
    val isRestDay: Boolean
        get() = type == "rest"
    
    val exerciseSetsFromSections: List<ExerciseSet>
        get() = sections.flatMap { it.exerciseSets }
}
