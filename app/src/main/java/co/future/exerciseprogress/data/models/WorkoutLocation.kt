package co.future.exerciseprogress.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class WorkoutLocation(
    @SerialName("latitude")
    val latitude: Double,

    @SerialName("longitude")
    val longitude: Double,

    @SerialName("recorded_at")
    val recordedAt: Instant? = null
)
