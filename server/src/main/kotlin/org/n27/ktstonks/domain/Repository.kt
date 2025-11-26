package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>

    suspend fun getStocks(): Result<Stocks>

    suspend fun searchStocks(query: String): Result<Stocks>

    suspend fun saveStock(stock: Stock): Result<Unit>
}
