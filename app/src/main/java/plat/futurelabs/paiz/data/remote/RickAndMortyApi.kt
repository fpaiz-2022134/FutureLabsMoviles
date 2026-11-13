package plat.futurelabs.paiz.data.remote

import plat.futurelabs.paiz.data.remote.dto.CharacterDto
import plat.futurelabs.paiz.data.remote.dto.LocationDto

interface RickAndMortyApi {

    suspend fun getAllCharacters(): List<CharacterDto>

    suspend fun getAllLocations(): List<LocationDto>
}