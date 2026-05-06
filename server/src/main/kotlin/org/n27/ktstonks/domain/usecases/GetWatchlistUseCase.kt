package org.n27.ktstonks.domain.usecases

import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stocks

class GetWatchlistUseCase(private val repository: Repository) {

    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
    ): Result<Stocks> = repository.getWatchlist(page, pageSize)
}
