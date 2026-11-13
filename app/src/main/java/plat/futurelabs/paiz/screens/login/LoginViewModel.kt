
package plat.futurelabs.paiz.screens.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.data.AppContainer

data class LoginUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val loginSuccessful: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val container = AppContainer(application)

    private val sessionRepository = container.sessionRepository

    private val syncRepository = container.syncRepository

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                error = null
            )
        }
    }

    fun login() {
        val name = _uiState.value.name.trim()

        if (name.isBlank()) {
            _uiState.update {
                it.copy(error = "Ingresa tu nombre")
            }
            return
        }

        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                Log.d(
                    "LOGIN_TEST",
                    "Iniciando login para el usuario: $name"
                )

                // Descargar datos desde la API
                // y almacenarlos en Room.
                syncRepository.syncInitialData()

                Log.d(
                    "LOGIN_TEST",
                    "Sincronización completada correctamente"
                )

                // Guardar sesión solamente cuando
                // la sincronización haya terminado.
                sessionRepository.login(name)

                Log.d(
                    "LOGIN_TEST",
                    "Sesión guardada correctamente"
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccessful = true
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                Log.e(
                    "LOGIN_ERROR",
                    "Error durante el inicio de sesión",
                    e
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error: ${
                            e.message ?: e.javaClass.simpleName
                        }"
                    )
                }
            }
        }
    }
}
