package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun updateStock(stock: Stock): Result<Unit>
    suspend fun getStocks(
        page: Int,
        pageSize: Int,
        filterWatchlist: Boolean,
        symbol: String?
    ): Result<Stocks>

    suspend fun addToWatchlist(symbol: String): Result<Unit>
    suspend fun getWatchlist(page: Int, pageSize: Int): Result<Stocks>
    suspend fun removeFromWatchlist(symbol: String): Result<Unit>
}
