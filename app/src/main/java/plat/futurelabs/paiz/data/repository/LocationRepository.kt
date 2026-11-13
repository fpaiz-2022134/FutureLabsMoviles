package plat.futurelabs.paiz.data.repository

import kotlinx.coroutines.flow.Flow
import plat.futurelabs.paiz.Location

interface LocationRepository {

    fun getAllLocations(): Flow<List<Location>>

    fun getLocationById(id: Int): Flow<Location?>

    fun getFavoriteLocations(): Flow<List<Location>>

    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )
}