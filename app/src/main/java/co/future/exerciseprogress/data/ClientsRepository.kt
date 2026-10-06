package co.future.exerciseprogress.data

import android.content.Context
import co.future.exerciseprogress.data.models.Client
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    val clients: List<Client> = loadClients()

    fun getClient(clientID: String): Client? {
        return clients.firstOrNull { it.id == clientID }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadClients(): List<Client> {
        return try {
            context.assets.open("clients.json").use { stream ->
                json.decodeFromStream<List<Client>>(stream)
            }
        } catch (e: Exception) {
            println("Failed to load clients: ${e.message}")
            emptyList()
        }
    }
}
