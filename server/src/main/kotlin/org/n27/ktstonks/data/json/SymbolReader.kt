package org.n27.ktstonks.data.json

class SymbolReader(private val jsonReader: JsonReader) {

    private lateinit var symbols: List<String>

    fun getSymbols(symbol: String?): List<String> {
        if (!this::symbols.isInitialized) {
            symbols = listOf("/sp.json", "/stoxx.json", "/nikkei.json", "/ibex.json", "/others.json")
                .flatMap { jsonReader.readJson<List<String>>(it) }
                .distinct()
        }

        return if (!symbol.isNullOrEmpty()) symbols.filter { it.contains(symbol) } else symbols
    }
}
