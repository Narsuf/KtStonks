package org.n27.ktstonks.data

import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

interface RemoteDataSource {
    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(page: Int, pageSize: Int, symbol: String? = null): Result<Stocks>
}
