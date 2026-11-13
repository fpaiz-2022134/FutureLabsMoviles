
package plat.futurelabs.paiz.navigation

import kotlinx.serialization.Serializable

@Serializable
data object StartupDestination

@Serializable
data object LoginDestination

@Serializable
data object MainDestination

@Serializable
data object CharactersGraph

@Serializable
data object CharactersDestination

@Serializable
data class CharacterDetailDestination(
    val id: Int
)

@Serializable
data class CharacterFormDestination(
    val id: Int? = null
)

@Serializable
data object LocationsGraph

@Serializable
data object LocationsDestination

@Serializable
data class LocationDetailDestination(
    val id: Int
)

@Serializable
data object ProfileDestination
