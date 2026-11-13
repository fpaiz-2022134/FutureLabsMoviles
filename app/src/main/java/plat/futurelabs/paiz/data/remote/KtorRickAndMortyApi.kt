
package plat.futurelabs.paiz.data.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import plat.futurelabs.paiz.data.remote.dto.ApiPageDto
import plat.futurelabs.paiz.data.remote.dto.CharacterDto
import plat.futurelabs.paiz.data.remote.dto.LocationDto

class KtorRickAndMortyApi(
    private val client: HttpClient
) : RickAndMortyApi {

    private val baseUrl = "https://rickandmortyapi.com/api"

    override suspend fun getAllCharacters(): List<CharacterDto> {
        return fetchAllPages("$baseUrl/character")
    }

    override suspend fun getAllLocations(): List<LocationDto> {
        return fetchAllPages("$baseUrl/location")
    }

    private suspend inline fun <reified T> fetchAllPages(
        initialUrl: String
    ): List<T> {

        val results = mutableListOf<T>()
        var nextUrl: String? = initialUrl
        var pageNumber = 1

        while (nextUrl != null) {

            val page = getPageWithRetry<T>(nextUrl)

            results.addAll(page.results)

            Log.d(
                "API_TEST",
                "Página $pageNumber descargada. " +
                        "Registros acumulados: ${results.size}"
            )

            nextUrl = page.info.next
            pageNumber++

            if (nextUrl != null) {
                delay(1000L)
            }
        }

        return results
    }

    private suspend inline fun <reified T> getPageWithRetry(
        url: String
    ): ApiPageDto<T> {

        var attempt = 0
        val maxRetries = 3

        while (true) {
            try {
                return client.get(url).body()

            } catch (e: ClientRequestException) {

                if (
                    e.response.status != HttpStatusCode.TooManyRequests ||
                    attempt >= maxRetries
                ) {
                    throw e
                }

                val retryAfterSeconds =
                    e.response.headers[HttpHeaders.RetryAfter]
                        ?.toLongOrNull()

                val waitSeconds = retryAfterSeconds
                    ?: (30L * (1L shl attempt))

                Log.w(
                    "API_TEST",
                    "HTTP 429. Esperando $waitSeconds segundos."
                )

                delay(waitSeconds * 1000L)
                attempt++
            }
        }
    }
}
