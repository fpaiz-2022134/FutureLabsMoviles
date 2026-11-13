
package plat.futurelabs.paiz.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import plat.futurelabs.paiz.data.AppContainer
import androidx.compose.ui.platform.LocalContext

@Composable
fun StartupScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val context = LocalContext.current

    val container = remember(context) {
        AppContainer(context.applicationContext)
    }

    LaunchedEffect(Unit) {
        try {
            val name = container.sessionRepository.userName.first()

            if (name.isNullOrBlank()) {
                onNavigateToLogin()
            } else if (container.syncRepository.hasInitialData()) {
                onNavigateToMain()
            } else {
                container.sessionRepository.logout()
                onNavigateToLogin()
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text("Preparando aplicación...")
        }
    }
}
