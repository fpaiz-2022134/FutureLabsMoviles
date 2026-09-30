package plat.futurelabs.paiz.screens.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.CharacterDb

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import plat.futurelabs.paiz.screens.login.LoginScreen
import plat.futurelabs.paiz.ui.theme.FutureLabsMovilesTheme

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val characters = CharacterDb().getAllCharacters()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters")
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                items = characters,
                key = { character -> character.id}
            ){character ->
                CharacterItem(
                    character = character,
                    onClick = {
                        onCharacterClick(character.id)
                    }
                )

                HorizontalDivider()
            }
        }

    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
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
        ){
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
    }
}

