package plat.futurelabs.paiz.data.repository

interface CatalogSyncRepository {

    suspend fun syncInitialData()

    suspend fun hasInitialData(): Boolean

    suspend fun clearAllData()
}