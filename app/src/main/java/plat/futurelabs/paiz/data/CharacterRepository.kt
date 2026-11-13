package plat.futurelabs.paiz.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.local.AppDatabase
import plat.futurelabs.paiz.data.mapper.toEntity
import plat.futurelabs.paiz.data.mapper.toModel
import plat.futurelabs.paiz.data.repository.CharacterRepository as CharacterRepositoryContract

class CharacterRepository(
    private val database: AppDatabase
) : CharacterRepositoryContract {

    private val dao = database.characterDao()

    override fun getAllCharacters(): Flow<List<Character>> =
        dao.getAllCharacters().map { entities ->
            entities.map { it.toModel() }
        }

    override fun getCharacterById(id: Int): Flow<Character?> =
        dao.getCharacterById(id).map { entity ->
            entity?.toModel()
        }

    override fun getFavoriteCharacters(): Flow<List<Character>> =
        dao.getFavoriteCharacters().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun createCharacter(
        character: Character
    ): Int = database.withTransaction {

        val newId = dao.getMaxId() + 1

        dao.insertCharacter(
            character.copy(id = newId).toEntity()
        )

        newId
    }

    override suspend fun updateCharacter(character: Character) {
        dao.updateCharacter(character.toEntity())
    }

    override suspend fun deleteCharacter(id: Int) {
        dao.deleteCharacterById(id)
    }

    override suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        dao.updateFavorite(id, isFavorite)
    }
}