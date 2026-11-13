
package plat.futurelabs.paiz.screens.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.screens.common.ErrorLayout
import plat.futurelabs.paiz.screens.common.LoadingLayout

@Composable
fun LocationsRoute(
    viewModel: LocationsViewModel,
    onLocationClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LocationsScreen(
        state = state,
        onLocationClick = onLocationClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onFilterChange = viewModel::setFilter,
        onLoadingClick = viewModel::showError,
        onRetryClick = viewModel::retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    state: LocationsUiState,
    onLocationClick: (Int) -> Unit,
    onFavoriteClick: (Location) -> Unit,
    onFilterChange: (LocationFilter) -> Unit,
    onLoadingClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Locations") })
        }
    ) { padding ->
        when {
            state.hasError -> ErrorLayout(
                onRetryClick = onRetryClick,
                modifier = Modifier.padding(padding)
            )

            state.isLoading -> LoadingLayout(
                onClick = onLoadingClick,
                modifier = Modifier.padding(padding)
            )

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LocationFilterSelector(
                    selected = state.filter,
                    onSelected = onFilterChange
                )

                if (state.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay ubicaciones para mostrar")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.data, key = { it.id }) { location ->
                            LocationListItem(
                                location = location,
                                onClick = {
                                    onLocationClick(location.id)
                                },
                                onFavorite = {
                                    onFavoriteClick(location)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationFilterSelector(
    selected: LocationFilter,
    onSelected: (LocationFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LocationFilter.entries.forEach { filter ->
            val label = when (filter) {
                LocationFilter.ALL -> "Todas"
                LocationFilter.FAVORITES -> "Favoritas"
                LocationFilter.NOT_FAVORITES -> "No favoritas"
            }

            FilterChip(
                selected = selected == filter,
                onClick = { onSelected(filter) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun LocationListItem(
    location: Location,
    onClick: () -> Unit,
    onFavorite: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    location.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Tipo: ${location.type}")
                Text("Dimensión: ${location.dimension}")
            }

            IconButton(onClick = onFavorite) {
                Text(
                    if (location.isFavorite) "♥" else "♡",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (location.isFavorite)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
