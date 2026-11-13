
package plat.futurelabs.paiz.data

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import plat.futurelabs.paiz.data.local.AppDatabase
import plat.futurelabs.paiz.data.local.CharacterImageStore
import plat.futurelabs.paiz.data.mapper.toEntity
import plat.futurelabs.paiz.data.remote.RickAndMortyApi
import plat.futurelabs.paiz.data.repository.CatalogSyncRepository

class SyncRepository(
    private val database: AppDatabase,
    private val api: RickAndMortyApi,
    private val imageStore: CharacterImageStore
) : CatalogSyncRepository {

    override suspend fun syncInitialData() {

        val startTime = System.currentTimeMillis()

        Log.d("SYNC_TEST", "1. Iniciando sincronización")

        delay(4000L)

        if (hasInitialData()) {
            Log.d(
                "SYNC_TEST",
                "Ya existen datos locales. No se sincroniza nuevamente."
            )
            return
        }

        Log.d("SYNC_TEST", "2. Descargando personajes")

        val characters = api.getAllCharacters()

        Log.d(
            "SYNC_TEST",
            "3. Personajes descargados: ${characters.size}"
        )

        Log.d("SYNC_TEST", "4. Descargando locations")

        val locations = api.getAllLocations()

        Log.d(
            "SYNC_TEST",
            "5. Locations descargadas: ${locations.size}"
        )

        require(characters.isNotEmpty()) {
            "No se recibieron personajes desde el API"
        }

        require(locations.isNotEmpty()) {
            "No se recibieron locations desde el API"
        }

        Log.d("SYNC_TEST", "6. Descargando imágenes")

        val semaphore = Semaphore(8)

        val localCharacters = coroutineScope {
            characters.map { dto ->
                async(Dispatchers.IO) {
                    semaphore.withPermit {
                        val localImage = try {
                            imageStore.download(
                                dto.id,
                                dto.image
                            )
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Log.w(
                                "SYNC_TEST",
                                "No se pudo descargar imagen ${dto.id}",
                                e
                            )
                            ""
                        }

                        dto.toEntity().copy(
                            image = localImage
                        )
                    }
                }
            }.awaitAll()
        }

        Log.d(
            "SYNC_TEST",
            "7. Imágenes procesadas: ${localCharacters.size}"
        )

        val localLocations = locations.map {
            it.toEntity()
        }

        Log.d("SYNC_TEST", "8. Guardando información en Room")

        database.withTransaction {
            database.characterDao().deleteAllCharacters()
            database.locationDao().deleteAllLocations()

            database.characterDao()
                .insertCharacters(localCharacters)

            database.locationDao()
                .insertLocations(localLocations)
        }

        Log.d(
            "SYNC_TEST",
            "9. Datos guardados correctamente"
        )

        val elapsedTime =
            System.currentTimeMillis() - startTime

        Log.d(
            "SYNC_TEST",
            "Sincronización completa en $elapsedTime ms"
        )
    }

    override suspend fun hasInitialData(): Boolean {
        return database.characterDao().countCharacters() > 0 &&
                database.locationDao().countLocations() > 0
    }

    override suspend fun clearAllData() {
        database.withTransaction {
            database.characterDao().deleteAllCharacters()
            database.locationDao().deleteAllLocations()
        }

        withContext(Dispatchers.IO) {
            imageStore.deleteAll()
        }
    }
}
