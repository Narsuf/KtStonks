package org.n27.ktstonks.domain.usecase

import org.n27.ktstonks.domain.Repository

class RemoveFromWatchlistUseCase(private val repository: Repository) {

    suspend operator fun invoke(symbol: String): Result<Unit> = repository.removeFromWatchlist(symbol)
}
