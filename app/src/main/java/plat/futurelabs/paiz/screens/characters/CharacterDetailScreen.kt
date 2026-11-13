
package plat.futurelabs.paiz.screens.characters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.screens.common.ErrorLayout
import plat.futurelabs.paiz.screens.common.LoadingLayout

@Composable
fun CharacterDetailRoute(
    viewModel: CharacterDetailViewModel,
    onBack: () -> Unit,
    onEditCharacter: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val deleted by viewModel.deleted.collectAsStateWithLifecycle()

    LaunchedEffect(deleted) {
        if (deleted) onBack()
    }

    CharacterDetailScreen(
        state = state,
        onBack = onBack,
        onEditCharacter = onEditCharacter,
        onFavoriteClick = viewModel::toggleFavorite,
        onDeleteClick = viewModel::deleteCharacter,
        onLoadingClick = viewModel::showError,
        onRetryClick = viewModel::retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    state: CharacterDetailUiState,
    onBack: () -> Unit,
    onEditCharacter: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLoadingClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Atrás")
                    }
                },
                actions = {
                    state.data?.let { character ->
                        IconButton(onClick = onFavoriteClick) {
                            Text(
                                if (character.isFavorite) "♥" else "♡",
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (character.isFavorite)
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

            state.data != null -> CharacterDetailContent(
                character = state.data,
                onEdit = { onEditCharacter(state.data.id) },
                onDelete = { showDeleteDialog = true },
                modifier = Modifier.padding(padding)
            )

            else -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Personaje no encontrado")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar personaje") },
            text = {
                Text("¿Estás seguro de eliminar este personaje?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun CharacterDetailContent(
    character: Character,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = characterImageModel(character.image),
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
        )

        Text(
            character.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Estado: ${character.status}")
        Text("Especie: ${character.species}")
        Text("Género: ${character.gender}")

        Button(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Editar personaje")
        }

        Button(
            onClick = onDelete,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Eliminar personaje")
        }
    }
}
