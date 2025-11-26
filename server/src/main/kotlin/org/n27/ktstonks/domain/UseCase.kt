package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

class UseCase(private val repository: Repository) {

    suspend fun execute(symbol: String): Result<Stock> = repository.getStock(symbol)

    suspend fun getStocks(): Result<Stocks> = repository.getStocks()
}
