package plat.futurelabs.paiz.data

import android.content.Context
import plat.futurelabs.paiz.data.local.CharacterImageStore
import plat.futurelabs.paiz.data.local.DatabaseProvider
import plat.futurelabs.paiz.data.remote.ApiClient
import plat.futurelabs.paiz.data.remote.KtorRickAndMortyApi

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val database =
        DatabaseProvider.getDatabase(appContext)

    private val api = KtorRickAndMortyApi(
        ApiClient.client
    )

    private val imageStore = CharacterImageStore(appContext)

    val characterRepository = CharacterRepository(database)

    val locationRepository = LocationRepository(
        database.locationDao()
    )

    val syncRepository = SyncRepository(
        database = database,
        api = api,
        imageStore = imageStore
    )

    val sessionRepository = SessionRepository(appContext)
}