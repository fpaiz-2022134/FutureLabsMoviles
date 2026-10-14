
package plat.futurelabs.paiz.screens.locations

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
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.LocationDb
import plat.futurelabs.paiz.navigation.LocationDetailDestination

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationDb = LocationDb()

    private val destination =
        savedStateHandle.toRoute<LocationDetailDestination>()

    private val locationId = destination.id

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
            _uiState.value = LocationDetailUiState(
                isLoading = true,
                data = null,
                hasError = false
            )

            delay(2000)

            _uiState.value = LocationDetailUiState(
                isLoading = false,
                data = locationDb.getLocationById(locationId),
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
        getLocation()
    }
}
