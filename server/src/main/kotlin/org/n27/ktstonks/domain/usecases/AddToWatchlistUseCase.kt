package org.n27.ktstonks.domain.usecases

import org.n27.ktstonks.domain.Repository

class AddToWatchlistUseCase(private val repository: Repository) {

    suspend operator fun invoke(symbol: String): Result<Unit> = repository.addToWatchlist(symbol)
}
