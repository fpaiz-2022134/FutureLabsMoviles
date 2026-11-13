
package plat.futurelabs.paiz.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.data.AppContainer

data class ProfileUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val logoutSuccessful: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val container = AppContainer(application)

    private val sessionRepository = container.sessionRepository

    private val syncRepository = container.syncRepository

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionRepository.userName.collect { name ->
                _uiState.update {
                    it.copy(name = name.orEmpty())
                }
            }
        }
    }

    fun logout() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                // Eliminar personajes y locations de Room,
                // incluyendo favoritos y cambios locales.
                // También limpiar las imágenes almacenadas.
                syncRepository.clearAllData()

                // Eliminar la sesión de DataStore.
                sessionRepository.logout()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        logoutSuccessful = true
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cerrar sesión"
                    )
                }
            }
        }
    }
}
