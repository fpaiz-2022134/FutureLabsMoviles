package plat.futurelabs.paiz.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.local.LocationDao
import plat.futurelabs.paiz.data.mapper.toModel

class LocationRepository(
    private val dao: LocationDao
) {

    fun getAllLocations(): Flow<List<Location>> {
        return dao.getAllLocations().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getLocationById(id: Int): Flow<Location?> {
        return dao.getLocationById(id).map { entity ->
            entity?.toModel()
        }
    }
}