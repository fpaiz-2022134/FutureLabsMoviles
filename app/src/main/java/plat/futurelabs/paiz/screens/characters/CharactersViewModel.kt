
package plat.futurelabs.paiz.screens.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.AppContainer

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val onlyFavorites: Boolean = false,
    val searchQuery: String = ""
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AppContainer(application).characterRepository

    private val _uiState = MutableStateFlow(CharactersUiState())

    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    private val onlyFavorites = MutableStateFlow(false)
    private val searchQuery = MutableStateFlow("")

    private var loadingJob: Job? = null

    init {
        getCharacters()
    }

    private fun getCharacters() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    hasError = false
                )
            }

            try {
                delay(4000L)

                combine(
                    repository.getAllCharacters(),
                    onlyFavorites,
                    searchQuery
                ) { characters, favoritesOnly, query ->

                    val filtered = characters.filter { character ->
                        val matchesFavorite =
                            !favoritesOnly || character.isFavorite

                        val matchesSearch =
                            character.name.contains(
                                query.trim(),
                                ignoreCase = true
                            )

                        matchesFavorite && matchesSearch
                    }.sortedByDescending { it.id }

                    CharactersUiState(
                        isLoading = false,
                        data = filtered,
                        hasError = false,
                        onlyFavorites = favoritesOnly,
                        searchQuery = query
                    )

                }.collect { state ->
                    _uiState.value = state
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun setFavoritesFilter(enabled: Boolean) {
        onlyFavorites.value = enabled
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            try {
                repository.updateFavorite(
                    character.id,
                    !character.isFavorite
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

    fun deleteCharacter(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCharacter(id)
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
        getCharacters()
    }
}
