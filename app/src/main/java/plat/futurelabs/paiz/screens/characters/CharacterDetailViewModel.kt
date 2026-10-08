
package plat.futurelabs.paiz.screens.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.CharacterDb
import plat.futurelabs.paiz.navigation.CharacterDetailDestination

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterDb = CharacterDb()

    private val destination =
        savedStateHandle.toRoute<CharacterDetailDestination>()

    private val characterId = destination.id

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
            _uiState.value = CharacterDetailUiState(
                isLoading = true,
                data = null,
                hasError = false
            )

            delay(2000)

            _uiState.value = CharacterDetailUiState(
                isLoading = false,
                data = characterDb.getCharacterById(characterId),
                hasError = false
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
