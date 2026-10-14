
package plat.futurelabs.paiz.navigation

import kotlinx.serialization.Serializable

// Navegación principal

@Serializable
data object StartupDestination

@Serializable
data object LoginDestination

@Serializable
data object MainDestination

// Characters nested graph

@Serializable
data object CharactersGraph

@Serializable
data object CharactersDestination

@Serializable
data class CharacterDetailDestination(
    val id: Int
)

// Locations nested graph

@Serializable
data object LocationsGraph

@Serializable
data object LocationsDestination

@Serializable
data class LocationDetailDestination(
    val id: Int
)

// Profile

@Serializable
data object ProfileDestination
