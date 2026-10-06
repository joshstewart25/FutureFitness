package co.future.exerciseprogress.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Client(
    @SerialName("id")
    val id: String,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}
