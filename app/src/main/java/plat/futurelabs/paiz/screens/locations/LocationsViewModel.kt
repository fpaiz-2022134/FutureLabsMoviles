
package plat.futurelabs.paiz.screens.locations

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
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.AppContainer

enum class LocationFilter {
    ALL,
    FAVORITES,
    NOT_FAVORITES
}

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false,
    val filter: LocationFilter = LocationFilter.ALL
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AppContainer(application).locationRepository

    private val _uiState = MutableStateFlow(LocationsUiState())

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private val selectedFilter = MutableStateFlow(LocationFilter.ALL)

    private var loadingJob: Job? = null

    init {
        getLocations()
    }

    private fun getLocations() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, hasError = false)
            }

            try {
                delay(4000L)

                combine(
                    repository.getAllLocations(),
                    selectedFilter
                ) { locations, filter ->

                    val filtered = when (filter) {
                        LocationFilter.ALL -> locations
                        LocationFilter.FAVORITES ->
                            locations.filter { it.isFavorite }
                        LocationFilter.NOT_FAVORITES ->
                            locations.filter { !it.isFavorite }
                    }

                    filtered to filter

                }.collect { (locations, filter) ->
                    _uiState.value = LocationsUiState(
                        isLoading = false,
                        data = locations,
                        hasError = false,
                        filter = filter
                    )
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

    fun setFilter(filter: LocationFilter) {
        selectedFilter.value = filter
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            try {
                repository.updateFavorite(
                    id = location.id,
                    isFavorite = !location.isFavorite
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(hasError = true) }
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
        getLocations()
    }
}
