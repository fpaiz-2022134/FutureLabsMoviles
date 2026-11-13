
package plat.futurelabs.paiz.data.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class CharacterImageStore(
    context: Context
) {
    private val directory = File(
        context.filesDir,
        "character_images"
    ).apply { mkdirs() }

    suspend fun download(
        id: Int,
        imageUrl: String
    ): String = withContext(Dispatchers.IO) {

        val connection = URL(imageUrl)
            .openConnection() as HttpURLConnection

        connection.connectTimeout = 15_000
        connection.readTimeout = 20_000

        try {
            connection.connect()

            if (connection.responseCode !in 200..299) {
                error("No se pudo descargar la imagen $id")
            }

            val file = File(directory, "$id.jpg")
            val temporary = File(directory, "$id.tmp")

            connection.inputStream.use { input ->
                temporary.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            if (!temporary.renameTo(file)) {
                temporary.copyTo(file, overwrite = true)
                temporary.delete()
            }

            file.absolutePath
        } finally {
            connection.disconnect()
        }
    }

    fun deleteAll() {
        directory.listFiles()?.forEach { it.delete() }
    }
}
