package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.extensions.isToday
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class UseCase(private val repository: Repository) {
    suspend fun getStock(symbol: String): Result<Stock> {
        val dbStock = repository.getDbStock(symbol).getOrNull()
        val isStockUpdated = dbStock?.lastUpdated?.isToday() ?: false

        return if (dbStock != null && isStockUpdated)
            success(dbStock)
        else
            repository.getStock(symbol).onSuccess { repository.saveStock(it) }
    }

    suspend fun getStocks(
        page: Int,
        pageSize: Int,
        filterWatchlist: Boolean,
        symbol: String? = null,
    ): Result<Stocks> {
        val result = if (symbol.isNullOrEmpty())
            repository.getStocks(page, pageSize)
        else
            getStocksBySymbol(page, pageSize, symbol)

        return if (filterWatchlist) {
            result.map { stocks ->
                stocks.copy(items = stocks.items.filter { !it.isWatchlisted })
            }
        } else {
            result
        }
    }

    private suspend fun getStocksBySymbol(page: Int, pageSize: Int, symbol: String): Result<Stocks> {
        val dbStocks = repository.searchStocks(symbol, page, pageSize).getOrNull()?.items

        if (!dbStocks.isNullOrEmpty()) return success(Stocks(dbStocks))

        val symbols = repository.getSymbols().getOrNull()
        val matchingSymbol = symbols?.items?.firstOrNull { it.equals(symbol, ignoreCase = true) }

        return if (matchingSymbol != null) {
            repository.getStock(matchingSymbol)
                .onSuccess { repository.saveStock(it) }
                .map { Stocks(listOf(it)) }
        } else {
            failure(NoSuchElementException("Stock with symbol $symbol not found"))
        }
    }

    suspend fun getWatchlist(
        page: Int,
        pageSize: Int,
        forceUpdate: Boolean,
    ): Result<Stocks> = runCatching {
        if (forceUpdate) {
            val allWatchlistStocks = repository.getWatchlist(page = 0, pageSize = Int.MAX_VALUE).getOrThrow().items
            val outdatedStocks = allWatchlistStocks
                .filter { !it.lastUpdated.isToday() }
                .sortedBy { it.lastUpdated }

            outdatedStocks.forEach loop@{ stock ->
                repository.getStock(stock.symbol)
                    .onSuccess { repository.saveStock(it) }
                    .onFailure { if (it is IllegalStateException) return@loop }
            }
        }

        repository.getWatchlist(page, pageSize).getOrThrow()
    }

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
