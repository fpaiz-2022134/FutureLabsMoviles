package plat.futurelabs.paiz.screens.profile


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.data.SessionRepository

data class ProfileUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val logoutSuccessful: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SessionRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.userName.collect { name ->
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
                repository.logout()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        logoutSuccessful = true
                    )
                }
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