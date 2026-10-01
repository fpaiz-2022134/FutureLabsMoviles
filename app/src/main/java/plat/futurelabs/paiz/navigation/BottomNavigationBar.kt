package plat.futurelabs.paiz.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedItem: BottomNavigationItem,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {

        // Characters

        NavigationBarItem(
            selected = selectedItem == BottomNavigationItem.CHARACTERS,
            onClick = onCharactersClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Characters"
                )
            },
            label = {
                Text("Characters")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor =
                    MaterialTheme.colorScheme.onSecondaryContainer,

                selectedTextColor =
                    MaterialTheme.colorScheme.onPrimaryContainer,

                indicatorColor =
                    MaterialTheme.colorScheme.secondaryContainer,

                unselectedIconColor =
                    MaterialTheme.colorScheme.onPrimaryContainer,

                unselectedTextColor =
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Locations

        NavigationBarItem(
            selected = selectedItem == BottomNavigationItem.LOCATIONS,
            onClick = onLocationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Locations"
                )
            },
            label = {
                Text("Locations")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor =
                    MaterialTheme.colorScheme.onSecondaryContainer,

                selectedTextColor =
                    MaterialTheme.colorScheme.onPrimaryContainer,

                indicatorColor =
                    MaterialTheme.colorScheme.secondaryContainer,

                unselectedIconColor =
                    MaterialTheme.colorScheme.onPrimaryContainer,

                unselectedTextColor =
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Profile
        NavigationBarItem(
            selected = selectedItem == BottomNavigationItem.PROFILE,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text("Profile")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor =
                    MaterialTheme.colorScheme.onSecondaryContainer,

                selectedTextColor =
                    MaterialTheme.colorScheme.onPrimaryContainer,

                indicatorColor =
                    MaterialTheme.colorScheme.secondaryContainer,

                unselectedIconColor =
                    MaterialTheme.colorScheme.onPrimaryContainer,

                unselectedTextColor =
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}

enum class BottomNavigationItem {
    CHARACTERS,
    LOCATIONS,
    PROFILE
}