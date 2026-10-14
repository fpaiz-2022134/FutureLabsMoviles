
package plat.futurelabs.paiz.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.LocationDb

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(
        LocationsUiState()
    )

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        getLocations()
    }

    private fun getLocations() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.value = LocationsUiState(
                isLoading = true,
                data = emptyList(),
                hasError = false
            )

            delay(4000)

            _uiState.value = LocationsUiState(
                isLoading = false,
                data = locationDb.getAllLocations(),
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
        getLocations()
    }
}
