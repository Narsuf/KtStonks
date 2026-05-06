package org.n27.ktstonks.domain.usecases

import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stocks.Stock

class GetStockUseCase(private val repository: Repository) {

    suspend operator fun invoke(symbol: String): Result<Stock> = repository.getStock(symbol)
}
