package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Symbols

interface Repository {

    suspend fun getRemoteStock(symbol: String): Result<Stock>

    suspend fun getStoredStocks(page: Int, pageSize: Int): Result<Stocks>
    suspend fun searchStoredStocks(symbol: String, page: Int, pageSize: Int): Result<Stocks>
    suspend fun getStoredStock(symbol: String): Result<Stock?>
    suspend fun saveStock(stock: Stock): Result<Unit>

    suspend fun addToWatchlist(symbol: String): Result<Unit>
    suspend fun getWatchlist(page: Int, pageSize: Int): Result<Stocks>
    suspend fun removeFromWatchlist(symbol: String): Result<Unit>

    suspend fun getSymbols(): Result<Symbols>
}
