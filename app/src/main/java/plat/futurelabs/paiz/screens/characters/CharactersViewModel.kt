package plat.futurelabs.paiz.screens.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.CharacterRepository
import plat.futurelabs.paiz.data.local.DatabaseProvider

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val onlyFavorites: Boolean = false
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(
        DatabaseProvider.getDatabase(application).characterDao()
    )

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    private val onlyFavorites = MutableStateFlow(false)
    private var loadingJob: Job? = null

    init {
        getCharacters()
    }

    private fun getCharacters() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                hasError = false
            )

            try {
                combine(
                    repository.getAllCharacters(),
                    onlyFavorites
                ) { characters, favoritesOnly ->
                    if (favoritesOnly) {
                        characters.filter { it.isFavorite }
                    } else {
                        characters
                    }
                }.collect { characters ->
                    _uiState.value = CharactersUiState(
                        isLoading = false,
                        data = characters,
                        hasError = false,
                        onlyFavorites = onlyFavorites.value
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun setFavoritesFilter(enabled: Boolean) {
        onlyFavorites.value = enabled
    }

    fun toggleFavorite(character: Character) {
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
        getCharacters()
    }
}