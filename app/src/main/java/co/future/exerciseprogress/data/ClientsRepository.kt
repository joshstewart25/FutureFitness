package co.future.exerciseprogress.data

import android.content.Context
import co.future.exerciseprogress.data.models.Client
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
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

    private fun loadClients(): List<Client> {
        return try {
            val clientsJsonString = context.assets.open("clients.json").bufferedReader().use { it.readText() }
            json.decodeFromString<List<Client>>(clientsJsonString)
        } catch (e: Exception) {
            println("Failed to load clients: ${e.message}")
            emptyList()
        }
    }
}
