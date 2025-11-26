package org.n27.ktstonks.domain

import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class UseCase(
    private val repository: Repository,
    private val jsonReader: JsonReader,
) {

    suspend fun execute(symbol: String): Result<Stock> = repository.getStock(symbol)

    suspend fun getStocks(): Result<Stocks> = repository.getStocks()

    suspend fun searchStock(query: String): Result<Stocks> {
        val dbResult = repository.searchStocks(query)
        val dbMatches = dbResult.getOrNull()?.items

        if (!dbMatches.isNullOrEmpty()) return success(Stocks(dbMatches))

        val jsonSymbols = jsonReader.getSymbols()
        val matchingSymbol = jsonSymbols.firstOrNull { it.equals(query, ignoreCase = true) }

        return if (matchingSymbol != null) {
            repository.getStock(matchingSymbol)
                .onSuccess { repository.saveStock(it) }
                .fold(
                    onSuccess = { success(Stocks(listOf(it))) },
                    onFailure = { failure(it) }
                )
        } else {
            failure(NoSuchElementException("Stock with query '$query' not found"))
        }
    }
}
