package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(page: Int, pageSize: Int): Result<Stocks>
    suspend fun searchStocks(symbol: String, page: Int, pageSize: Int): Result<Stocks>
    suspend fun getDbStock(symbol: String): Result<Stock?>
    suspend fun saveStock(stock: Stock): Result<Unit>
}
