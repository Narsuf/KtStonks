package org.n27.ktstonks.data.json

class SymbolReader(private val jsonReader: JsonReader) {

    private lateinit var symbols: List<String>

    suspend fun getSymbols(symbol: String?): List<String> {
        if (!this::symbols.isInitialized) {
            val sp = readSymbols("/sp.json")
            val stoxx = readSymbols("/stoxx.json")
            val nikkei = readSymbols("/nikkei.json")
            val ibex = readSymbols("/ibex.json")
            val others = readSymbols("/others.json")

            symbols = (sp + stoxx + nikkei + ibex + others).distinct()
        }

        return if (!symbol.isNullOrEmpty())
            symbols.filter { it.contains(symbol) }
        else
            symbols
    }

    private suspend fun readSymbols(fileName: String): List<String> {
        val symbols: List<String> = jsonReader.readJson(fileName)
        return symbols
    }
}
