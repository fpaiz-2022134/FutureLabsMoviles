
package plat.futurelabs.paiz.screens.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.CharacterRepository
import plat.futurelabs.paiz.data.local.DatabaseProvider
import plat.futurelabs.paiz.navigation.CharacterDetailDestination

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(
        DatabaseProvider.getDatabase(application)
    )

    private val characterId = savedStateHandle
        .toRoute<CharacterDetailDestination>().id

    private val _uiState = MutableStateFlow(
        CharacterDetailUiState()
    )

    val uiState: StateFlow<CharacterDetailUiState> =
        _uiState.asStateFlow()

    private val _deleted = MutableStateFlow(false)

    val deleted: StateFlow<Boolean> =
        _deleted.asStateFlow()

    private var loadingJob: Job? = null

    init {
        getCharacter()
    }

    private fun getCharacter() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {

            _uiState.value = CharacterDetailUiState()

            try {
                delay(2000L)

                // Observamos Room continuamente
                repository.getCharacterById(characterId)
                    .collect { character ->

                        _uiState.value = CharacterDetailUiState(
                            isLoading = false,
                            data = character,
                            hasError = character == null
                        )
                    }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CharacterDetailUiState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun toggleFavorite() {
        val character = _uiState.value.data ?: return

        viewModelScope.launch {
            try {
                repository.updateFavorite(
                    id = character.id,
                    isFavorite = !character.isFavorite
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(hasError = true)
                }
            }
        }
    }

    fun deleteCharacter() {
        val character = _uiState.value.data ?: return

        viewModelScope.launch {
            try {
                repository.deleteCharacter(character.id)

                // Indicamos a la UI que debe regresar al listado
                _deleted.value = true

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(hasError = true)
                }
            }
        }
    }

    fun showError() {
        if (!_uiState.value.isLoading) return

        loadingJob?.cancel()

        _uiState.update {
            it.copy(
                isLoading = false,
                hasError = true
            )
        }
    }

    fun retry() {
        getCharacter()
    }
}
