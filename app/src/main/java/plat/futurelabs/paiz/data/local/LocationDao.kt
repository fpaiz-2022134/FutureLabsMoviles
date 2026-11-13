package plat.futurelabs.paiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations ORDER BY id ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    @Query(
        "SELECT * FROM locations " +
                "WHERE isFavorite = 1 ORDER BY id ASC"
    )
    fun getFavoriteLocations(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(
        locations: List<LocationEntity>
    )

    @Query(
        "UPDATE locations SET isFavorite = :isFavorite " +
                "WHERE id = :id"
    )
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun countLocations(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()
}