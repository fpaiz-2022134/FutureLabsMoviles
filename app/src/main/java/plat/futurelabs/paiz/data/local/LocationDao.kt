package plat.futurelabs.paiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao{
    @Query("SELECT * FROM locations ORDER By id ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getLocationById(id: Int): Flow<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(
        locations: List<LocationEntity>
    )

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun countLocations(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()

}