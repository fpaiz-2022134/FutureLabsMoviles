
package plat.futurelabs.paiz.screens.locations

import androidx.compose.foundation.layout.*
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
fun LocationDetailRoute(
    viewModel: LocationDetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LocationDetailScreen(
        state = state,
        onBack = onBack,
        onFavoriteClick = viewModel::toggleFavorite,
        onLoadingClick = viewModel::showError,
        onRetryClick = viewModel::retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    state: LocationDetailUiState,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLoadingClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Detail") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Atrás")
                    }
                },
                actions = {
                    state.data?.let { location ->
                        IconButton(onClick = onFavoriteClick) {
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
            )
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

            state.data != null -> LocationDetailContent(
                location = state.data,
                modifier = Modifier.padding(padding)
            )

            else -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Ubicación no encontrada")
            }
        }
    }
}

@Composable
private fun LocationDetailContent(
    location: Location,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            location.name,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        Text(
            "Tipo: ${location.type}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            "Dimensión: ${location.dimension}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            if (location.isFavorite)
                "Esta ubicación está en tus favoritas"
            else
                "Esta ubicación no está en tus favoritas"
        )
    }
}
