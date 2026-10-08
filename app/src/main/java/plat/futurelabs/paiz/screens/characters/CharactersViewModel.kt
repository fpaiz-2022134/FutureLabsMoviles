package plat.futurelabs.paiz.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.CharacterDb
import kotlinx.coroutines.Job

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {

    private val characterDb = CharacterDb()

    private var loadingJob: Job? = null

    private val _uiState = MutableStateFlow(
        CharactersUiState()
    )

    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    init {
        getCharacters()
    }

    private fun getCharacters() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {

            _uiState.value = CharactersUiState(
                isLoading = true,
                data = emptyList(),
                hasError = false
            )

            delay(4000)

            _uiState.value = CharactersUiState(
                isLoading = false,
                data = characterDb.getAllCharacters(),
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
        getCharacters()
    }
}