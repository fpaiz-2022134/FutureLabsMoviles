package plat.futurelabs.paiz.screens.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.screens.common.ErrorLayout
import plat.futurelabs.paiz.screens.common.LoadingLayout

@Composable
fun CharactersRoute(
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.hasError -> {
            ErrorLayout(
                onRetryClick = viewModel::retry,
                modifier = modifier
            )
        }

        uiState.isLoading -> {
            LoadingLayout(
                onClick = viewModel::showError,
                modifier = modifier
            )
        }

        else -> {
            CharactersScreen(
                characters = uiState.data,
                onlyFavorites = uiState.onlyFavorites,
                onFavoritesFilterChange =
                    viewModel::setFavoritesFilter,
                onFavoriteClick = viewModel::toggleFavorite,
                onCharacterClick = onCharacterClick,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    characters: List<Character>,
    onlyFavorites: Boolean,
    onFavoritesFilterChange: (Boolean) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Characters") }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = onlyFavorites,
                    onCheckedChange = onFavoritesFilterChange
                )

                Text("Mostrar solo favoritos")
            }

            HorizontalDivider()

            if (characters.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (onlyFavorites) {
                            "No hay favoritos para mostrar"
                        } else {
                            "No hay personajes para mostrar"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = characters,
                        key = { it.id }
                    ) { character ->

                        CharacterItem(
                            character = character,
                            onClick = {
                                onCharacterClick(character.id)
                            },
                            onFavoriteClick = {
                                onFavoriteClick(character)
                            }
                        )

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = "Imagen de ${character.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Species: ${character.species}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Status: ${character.status}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Gender: ${character.gender}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (character.isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = if (character.isFavorite) {
                    "Quitar de favoritos"
                } else {
                    "Agregar a favoritos"
                },
                tint = if (character.isFavorite) {
                    Color.Red
                } else {
                    LocalContentColor.current
                }
            )
        }
    }
}