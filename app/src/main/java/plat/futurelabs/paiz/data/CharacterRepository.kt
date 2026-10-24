package plat.futurelabs.paiz.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.local.CharacterDao
import plat.futurelabs.paiz.data.mapper.toModel

class CharacterRepository(
    private val dao: CharacterDao
){
    fun getAllCharacters(): Flow<List<Character>>{
        return dao.getAllCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getCharacterById(id: Int): Flow<Character?>{
        return dao.getCharacterById(id).map{entity ->
            entity?.toModel()
        }
    }

    fun getFavoriteCharacters(): Flow<List<Character>>{
        return dao.getFavoriteCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        dao.updateFavorite(id, isFavorite)
    }
}
