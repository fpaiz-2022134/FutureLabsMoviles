package plat.futurelabs.paiz.AppNavigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

@Serializable
data object CharactersDestination

@Serializable
data class CharacterDetailDestination(
    val id: Int
)