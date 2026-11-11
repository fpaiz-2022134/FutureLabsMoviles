package plat.futurelabs.paiz.screens.locations

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
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.LocationRepository
import plat.futurelabs.paiz.data.local.DatabaseProvider
import plat.futurelabs.paiz.navigation.LocationDetailDestination
import kotlinx.coroutines.delay

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = LocationRepository(
        DatabaseProvider.getDatabase(application).locationDao()
    )

    private val locationId = savedStateHandle
        .toRoute<LocationDetailDestination>().id

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