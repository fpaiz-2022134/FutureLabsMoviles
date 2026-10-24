package plat.futurelabs.paiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterEntity>

    @Query(
        "SELECT * FROM characters " +
        "WHERE isFavorite = 1 ORDER BY id ASC"
    )
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacters(
        characters: List<CharacterEntity>
    )

    @Query(
        "UPDATE characters " +
        "SET isFavorite = :isFavorite WHERE id = :id"
    )
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun countCharacters(): Int

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()


}