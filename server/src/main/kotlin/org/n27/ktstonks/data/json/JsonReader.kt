package org.n27.ktstonks.data.json

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

object JsonReader {

    private lateinit var symbols: List<String>

    suspend fun getSymbols(): List<String> {
        if (!this::symbols.isInitialized) {
            val sp = readSymbols("/sp.json")
            val stoxx = readSymbols("/stoxx.json")
            val nikkei = readSymbols("/nikkei.json")
            val ibex = readSymbols("/ibex.json")
            val others = readSymbols("/others.json")

            symbols = (sp + stoxx + nikkei + ibex + others).distinct()
        }

        return symbols
    }

    private suspend fun readSymbols(fileName: String): List<String> {
        val symbols: List<String> = readJson(fileName)
        return symbols
    }

    private suspend inline fun <reified T> readJson(fileName: String): T = withContext(Dispatchers.IO) {
        val inputStream: InputStream = object {}.javaClass.getResourceAsStream(fileName)
            ?: throw IllegalStateException("Could not find resource file '$fileName'.")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Json.decodeFromString(jsonString)
    }
}
