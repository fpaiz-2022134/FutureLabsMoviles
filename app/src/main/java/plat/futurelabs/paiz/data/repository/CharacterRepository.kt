package plat.futurelabs.paiz.data.repository

import kotlinx.coroutines.flow.Flow
import plat.futurelabs.paiz.Character

interface CharacterRepository {

    fun getAllCharacters(): Flow<List<Character>>

    fun getCharacterById(id: Int): Flow<Character?>

    fun getFavoriteCharacters(): Flow<List<Character>>

    suspend fun createCharacter(character: Character): Int

    suspend fun updateCharacter(character: Character)

    suspend fun deleteCharacter(id: Int)

    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )
}