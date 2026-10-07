package plat.futurelabs.paiz.screens.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.LocationDb

@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val locations = LocationDb().getAllLocations()

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ){
        items(
            items = locations,
            key = { location ->
                location.id
            }
        ){
            location ->
            LocationItem(
                location = location,
                onClick = {
                    onLocationClick(location.id)
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun LocationItem(
    location: Location,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = location.name,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = location.type,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}