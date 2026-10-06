package co.future.exerciseprogress.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class HeartRateSample(
    @SerialName("recorded_at")
    val recordedAt: Instant,

    @SerialName("value")
    val value: Int
)
