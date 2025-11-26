package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock

class UseCase(private val repository: StockRepository) {

    suspend fun execute(symbol: String): Result<Stock> = repository.getStock(symbol)
}

