package plat.futurelabs.paiz.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.local.LocationDao
import plat.futurelabs.paiz.data.mapper.toModel
import plat.futurelabs.paiz.data.repository.LocationRepository as LocationRepositoryContract

class LocationRepository(
    private val dao: LocationDao
) : LocationRepositoryContract {

    override fun getAllLocations(): Flow<List<Location>> =
        dao.getAllLocations().map { entities ->
            entities.map { it.toModel() }
        }

    override fun getLocationById(id: Int): Flow<Location?> =
        dao.getLocationById(id).map { entity ->
            entity?.toModel()
        }

    override fun getFavoriteLocations(): Flow<List<Location>> =
        dao.getFavoriteLocations().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        dao.updateFavorite(id, isFavorite)
    }
}