package plat.futurelabs.paiz.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import plat.futurelabs.paiz.data.local.SessionDataStore

class SessionRepository(
    context: Context
) {

    private val sessionDataStore = SessionDataStore(context)

    val userName: Flow<String?> = sessionDataStore.userName

    suspend fun login(name: String) {
        val cleanName = name.trim()

        require(cleanName.isNotEmpty()) {
            "El nombre no puede estar vacío"
        }

        sessionDataStore.saveName(cleanName)
    }

    suspend fun logout() {
        sessionDataStore.clearName()
    }
}