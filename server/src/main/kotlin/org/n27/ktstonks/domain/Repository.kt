package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock

interface StockRepository {

    suspend fun getStock(symbol: String): Result<Stock>
}
