package plat.futurelabs.paiz.screens.profile

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

data class ProfileUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val logoutSuccessful: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionRepository =
        SessionRepository(application)

    private val syncRepository = SyncRepository(
        DatabaseProvider.getDatabase(application)
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

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
                it.copy(isLoading = true, error = null)
            }

            try {
                // Eliminar ambas tablas de Room.
                syncRepository.clearAllData()

                // Eliminar el nombre de DataStore.
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