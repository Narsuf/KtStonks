package org.n27.ktstonks.data.json

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

class JsonReader {

    suspend inline fun <reified T> readJson(fileName: String): T = withContext(Dispatchers.IO) {
        val inputStream: InputStream = javaClass.getResourceAsStream(fileName)
            ?: throw IllegalStateException("Could not find resource file '$fileName'.")
        val jsonString = inputStream.use { it.bufferedReader().readText() }
        Json.decodeFromString(jsonString)
    }
}
