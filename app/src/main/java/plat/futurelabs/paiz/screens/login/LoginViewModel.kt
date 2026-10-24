package plat.futurelabs.paiz.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.data.SessionRepository
import plat.futurelabs.paiz.data.SyncRepository
import plat.futurelabs.paiz.data.local.DatabaseProvider

data class LoginUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val loginSuccessful: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionRepository =
        SessionRepository(application)

    private val database =
        DatabaseProvider.getDatabase(application)

    private val syncRepository =
        SyncRepository(database)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(name = name, error = null)
        }
    }

    fun login() {
        val name = _uiState.value.name.trim()

        if (name.isBlank() || _uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            try {
                // Sincronización inicial con delay de 4 segundos
                syncRepository.syncInitialData()

                // Guardar la sesión solo cuando Room esté listo
                sessionRepository.login(name)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccessful = true
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo iniciar sesión"
                    )
                }
            }
        }
    }
}