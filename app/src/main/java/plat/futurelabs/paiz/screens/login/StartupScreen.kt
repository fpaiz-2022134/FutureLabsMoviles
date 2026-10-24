package plat.futurelabs.paiz.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.first
import plat.futurelabs.paiz.data.SessionRepository
import plat.futurelabs.paiz.data.SyncRepository
import plat.futurelabs.paiz.data.local.DatabaseProvider

@Composable
fun StartupScreen(
    onSessionFound: () -> Unit,
    onSessionNotFound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sessionRepository = SessionRepository(context)

        val name = sessionRepository.userName.first()

        if (name.isNullOrBlank()) {
            onSessionNotFound()
        } else {
            val database = DatabaseProvider.getDatabase(context)
            val syncRepository = SyncRepository(database)

            try {
                if (!syncRepository.hasInitialData()) {
                    syncRepository.insertMissingData()
                }

                if (syncRepository.hasInitialData()) {
                    onSessionFound()
                } else {
                    onSessionNotFound()
                }
            } catch (e: Exception) {
                onSessionNotFound()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text("Cargando sesión...")
    }
}