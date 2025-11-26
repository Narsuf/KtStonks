package org.n27.ktstonks.domain

import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.extensions.isToday
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class UseCase(
    private val repository: Repository,
    private val jsonReader: JsonReader,
) {
    suspend fun getStock(symbol: String): Result<Stock> {
        val dbStock = repository.getDbStock(symbol).getOrNull()
        val isStockUpdated = dbStock?.lastUpdated?.isToday() ?: false

        return if (dbStock != null && isStockUpdated)
            success(dbStock)
        else
            repository.getStock(symbol).onSuccess { repository.saveStock(it) }
    }

    suspend fun getStocks(page: Int = 0, pageSize: Int): Result<Stocks> = repository.getStocks(page, pageSize)

    suspend fun searchStock(symbol: String, page: Int = 0, pageSize: Int): Result<Stocks> {
        val dbStocks = repository.searchStocks(symbol, page, pageSize).getOrNull()?.items

        if (!dbStocks.isNullOrEmpty()) return success(Stocks(dbStocks))

        val symbols = jsonReader.getSymbols()
        val matchingSymbol = symbols.firstOrNull { it.equals(symbol, ignoreCase = true) }

        return if (matchingSymbol != null) {
            repository.getStock(matchingSymbol)
                .onSuccess { repository.saveStock(it) }
                .fold(
                    onSuccess = { success(Stocks(listOf(it))) },
                    onFailure = { failure(it) }
                )
        } else {
            failure(NoSuchElementException("Stock with symbol $symbol not found"))
        }
    }

    suspend fun getWatchlist(page: Int = 0, pageSize: Int): Result<Stocks> = repository.getWatchlist(page, pageSize)

    suspend fun addStockToWatchlist(symbol: String): Result<Unit> {
        return getStock(symbol).fold(
            onSuccess = { repository.addToWatchlist(symbol) },
            onFailure = { failure(it) }
        )
    }

    suspend fun removeStockFromWatchlist(symbol: String): Result<Unit> {
        return getStock(symbol).fold(
            onSuccess = { repository.removeFromWatchlist(symbol) },
            onFailure = { failure(it) }
        )
    }
}
