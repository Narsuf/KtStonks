package org.n27.ktstonks.domain

import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

class UseCase(
    private val repository: Repository,
    private val jsonReader: JsonReader,
) {

    suspend fun execute(symbol: String): Result<Stock> = repository.getStock(symbol)

    suspend fun getStocks(): Result<Stocks> = repository.getStocks()

    suspend fun searchStock(query: String): Result<Stock> {
        val dbResult = repository.searchStocks(query)
        dbResult.getOrNull()?.items?.firstOrNull()?.let { return Result.success(it) }

        val jsonSymbols = jsonReader.getSymbols()
        val matchingSymbol = jsonSymbols.firstOrNull { it.contains(query, ignoreCase = true) }

        return if (matchingSymbol != null) {
            repository.getStock(matchingSymbol)
        } else {
            Result.failure(NoSuchElementException("Stock with query '$query' not found"))
        }
    }
}
