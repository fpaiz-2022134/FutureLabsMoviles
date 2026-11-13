
package plat.futurelabs.paiz.screens.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.screens.common.ErrorLayout
import plat.futurelabs.paiz.screens.common.LoadingLayout
import java.io.File

@Composable
fun CharactersRoute(
    viewModel: CharactersViewModel,
    onCharacterClick: (Int) -> Unit,
    onCreateCharacter: () -> Unit,
    onEditCharacter: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CharactersScreen(
        state = state,
        onCharacterClick = onCharacterClick,
        onCreateCharacter = onCreateCharacter,
        onEditCharacter = onEditCharacter,
        onFavoriteClick = viewModel::toggleFavorite,
        onFilterChange = viewModel::setFavoritesFilter,
        onSearchChange = viewModel::onSearchQueryChange,
        onLoadingClick = viewModel::showError,
        onRetryClick = viewModel::retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    state: CharactersUiState,
    onCharacterClick: (Int) -> Unit,
    onCreateCharacter: () -> Unit,
    onEditCharacter: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    onFilterChange: (Boolean) -> Unit,
    onSearchChange: (String) -> Unit,
    onLoadingClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") }
            )
        },
        floatingActionButton = {
            if (!state.isLoading && !state.hasError) {
                FloatingActionButton(
                    onClick = onCreateCharacter
                ) {
                    Text(
                        "+",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
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
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchChange,
                    label = { Text("Buscar personaje") },
                    placeholder = { Text("Ej. Rick, Morty...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.onlyFavorites,
                        onCheckedChange = onFilterChange
                    )

                    Text("Mostrar solo favoritos")
                }

                if (state.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (state.searchQuery.isNotBlank())
                                "No se encontraron personajes"
                            else if (state.onlyFavorites)
                                "No hay favoritos para mostrar"
                            else
                                "No hay personajes para mostrar"
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 88.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            state.data,
                            key = { it.id }
                        ) { character ->

                            CharacterListItem(
                                character = character,
                                onClick = {
                                    onCharacterClick(character.id)
                                },
                                onEdit = {
                                    onEditCharacter(character.id)
                                },
                                onFavorite = {
                                    onFavoriteClick(character)
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
private fun CharacterListItem(
    character: Character,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onFavorite: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = characterImageModel(character.image),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(84.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    character.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text("Especie: ${character.species}")
                Text("Estado: ${character.status}")
                Text("Género: ${character.gender}")

                TextButton(onClick = onEdit) {
                    Text("Editar")
                }
            }

            IconButton(onClick = onFavorite) {
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
}

fun characterImageModel(path: String): Any? {
    if (path.isBlank()) return null

    return if (path.startsWith("/")) {
        File(path)
    } else {
        path
    }
}
