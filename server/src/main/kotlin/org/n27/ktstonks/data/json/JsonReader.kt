package org.n27.ktstonks.data.json

import kotlinx.serialization.json.Json
import java.io.InputStream

class JsonReader {

    inline fun <reified T> readJson(fileName: String): T {
        val inputStream: InputStream = javaClass.getResourceAsStream(fileName)
            ?: throw IllegalStateException("Could not find resource file '$fileName'.")
        val jsonString = inputStream.use { it.bufferedReader().readText() }
        return Json.decodeFromString(jsonString)
    }
}
