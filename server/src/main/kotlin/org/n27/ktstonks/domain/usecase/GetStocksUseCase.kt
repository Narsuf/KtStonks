package org.n27.ktstonks.domain.usecase

import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stocks

class GetStocksUseCase(private val repository: Repository) {

    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        filterWatchlist: Boolean,
        symbol: String?,
    ): Result<Stocks> = repository.getStocks(page, pageSize, filterWatchlist, symbol)
}
