package plat.futurelabs.paiz.screens.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.LocationRepository
import plat.futurelabs.paiz.data.local.DatabaseProvider
import kotlinx.coroutines.delay


data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = LocationRepository(
        DatabaseProvider.getDatabase(application).locationDao()
    )

    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        getLocations()
    }

    private fun getLocations() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.value = LocationsUiState()

            try {
                delay(4000L)

                repository.getAllLocations().collect { locations ->
                    _uiState.value = LocationsUiState(
                        isLoading = false,
                        data = locations,
                        hasError = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LocationsUiState(
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
        getLocations()
    }
}