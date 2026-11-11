package plat.futurelabs.paiz.data

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import plat.futurelabs.paiz.CharacterDb
import plat.futurelabs.paiz.LocationDb
import plat.futurelabs.paiz.data.local.AppDatabase
import plat.futurelabs.paiz.data.mapper.toEntity

class SyncRepository(
    private val database: AppDatabase
) {
    suspend fun syncInitialData(){
        delay(4000L)
        insertMissingData()
    }

    suspend fun insertMissingData(){
        database.withTransaction {
            val characterDao = database.characterDao()
            val locationDao = database.locationDao()

            if(characterDao.countCharacters()==0){
                val characters = CharacterDb()
                    .getAllCharacters()
                    .map { it.toEntity() }
                characterDao.insertCharacters(characters)
            }

            if (locationDao.countLocations() == 0) {
                val locations = LocationDb()
                    .getAllLocations()
                    .map { it.toEntity() }

                locationDao.insertLocations(locations)
            }
        }
    }

    suspend fun hasInitialData(): Boolean {
        return database.characterDao().countCharacters() > 0 &&
                database.locationDao().countLocations() > 0
    }

    suspend fun clearAllData() {
        database.withTransaction {
            database.characterDao().deleteAllCharacters()
            database.locationDao().deleteAllLocations()
        }
    }
}