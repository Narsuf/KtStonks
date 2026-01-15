package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock

class UseCase(private val repository: Repository) {

    suspend fun getStock(symbol: String): Result<Stock> = repository.getStock(symbol)

    suspend fun getStocks(
        page: Int,
        pageSize: Int,
        filterWatchlist: Boolean,
        symbol: String?,
    ): Result<Stocks> = repository.getStocks(page, pageSize, filterWatchlist, symbol)

    suspend fun getWatchlist(page: Int, pageSize: Int): Result<Stocks> = repository.getWatchlist(page, pageSize)

    suspend fun addToWatchlist(symbol: String): Result<Unit> = repository.addToWatchlist(symbol)

    suspend fun removeFromWatchlist(symbol: String): Result<Unit> = repository.removeFromWatchlist(symbol)

    suspend fun addCustomValuation(
        symbol: String,
        valuationFloor: Double?,
        epsGrowth: Double,
    ): Result<Unit> = repository.getStock(symbol).mapCatching {
        val updatedStock = it.copy(
            expectedEpsGrowth = epsGrowth,
            valuationFloor = valuationFloor,
        )
        repository.updateStock(updatedStock).getOrThrow()
    }
}
