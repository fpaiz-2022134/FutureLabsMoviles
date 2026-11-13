package plat.futurelabs.paiz.screens.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.repository.CharacterRepository
import plat.futurelabs.paiz.navigation.CharacterFormDestination

data class CharacterFormUiState(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class CharacterFormViewModel(
    private val repository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId =
        savedStateHandle.toRoute<CharacterFormDestination>().id

    private var originalCharacter: Character? = null

    private val _uiState = MutableStateFlow(
        CharacterFormUiState()
    )

    val uiState: StateFlow<CharacterFormUiState> =
        _uiState.asStateFlow()

    init {
        if (characterId != null) {
            loadCharacter(characterId)
        }
    }

    private fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val character =
                    repository.getCharacterById(id).first()

                if (character == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Personaje no encontrado"
                        )
                    }
                    return@launch
                }

                originalCharacter = character

                _uiState.value = CharacterFormUiState(
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    gender = character.gender,
                    image = character.image
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cargar el personaje"
                    )
                }
            }
        }
    }

    fun updateName(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun updateStatus(value: String) {
        _uiState.update { it.copy(status = value) }
    }

    fun updateSpecies(value: String) {
        _uiState.update { it.copy(species = value) }
    }

    fun updateGender(value: String) {
        _uiState.update { it.copy(gender = value) }
    }

    fun updateImage(value: String) {
        _uiState.update { it.copy(image = value) }
    }

    fun save() {
        if (_uiState.value.isSaving) return

        val state = _uiState.value

        if (
            state.name.isBlank() ||
            state.status.isBlank() ||
            state.species.isBlank() ||
            state.gender.isBlank()
        ) {
            _uiState.update {
                it.copy(error = "Completa los campos obligatorios")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, error = null)
            }

            try {
                val character = Character(
                    id = characterId ?: 0,
                    name = state.name.trim(),
                    status = state.status.trim(),
                    species = state.species.trim(),
                    gender = state.gender.trim(),
                    image = state.image.trim(),
                    isFavorite = originalCharacter?.isFavorite ?: false
                )

                if (characterId == null) {
                    repository.createCharacter(character)
                } else {
                    repository.updateCharacter(character)
                }

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saved = true
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "No se pudo guardar el personaje"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}