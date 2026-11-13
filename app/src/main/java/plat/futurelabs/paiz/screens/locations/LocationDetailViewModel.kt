
package plat.futurelabs.paiz.screens.locations

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
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.AppContainer
import plat.futurelabs.paiz.navigation.LocationDetailDestination

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository =
        AppContainer(application).locationRepository

    private val locationId =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val _uiState = MutableStateFlow(
        LocationDetailUiState()
    )

    val uiState: StateFlow<LocationDetailUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        getLocation()
    }

    private fun getLocation() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.value = LocationDetailUiState()

            try {
                delay(2000L)

                repository.getLocationById(locationId)
                    .collect { location ->
                        _uiState.value = LocationDetailUiState(
                            isLoading = false,
                            data = location,
                            hasError = location == null
                        )
                    }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LocationDetailUiState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun toggleFavorite() {
        val location = _uiState.value.data ?: return

        viewModelScope.launch {
            try {
                repository.updateFavorite(
                    id = location.id,
                    isFavorite = !location.isFavorite
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
        getLocation()
    }
}
