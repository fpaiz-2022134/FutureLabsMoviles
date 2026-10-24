package plat.futurelabs.paiz.screens.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        DatabaseProvider.getDatabase(application).characterDao()
    )

    private val characterId = savedStateHandle
        .toRoute<CharacterDetailDestination>().id

    private val _uiState = MutableStateFlow(
        CharacterDetailUiState()
    )

    val uiState: StateFlow<CharacterDetailUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        getCharacter()
    }

    private fun getCharacter() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.value = CharacterDetailUiState()

            try {
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
            repository.updateFavorite(
                id = character.id,
                isFavorite = !character.isFavorite
            )
        }
    }

    fun showError() {
        if (!_uiState.value.isLoading) return

        loadingJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            hasError = true
        )
    }

    fun retry() {
        getCharacter()
    }
}