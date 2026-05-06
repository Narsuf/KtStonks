package org.n27.ktstonks.domain.usecases

import org.n27.ktstonks.domain.Repository

class AddCustomValuationUseCase(private val repository: Repository) {

    suspend operator fun invoke(
        symbol: String,
        valuationFloor: Double,
    ): Result<Unit> = repository.getStock(symbol).mapCatching { stock ->
        val intrinsicValue = stock.valuationMeasures.pe
            ?.takeIf { it > 0 }
            ?.let { (stock.price ?: 0.0) * (valuationFloor / it) }
            ?: 0.0

        repository.updateStock(
            stock.copy(
                valuationMeasures = stock.valuationMeasures.copy(
                    valuationFloor = valuationFloor,
                    intrinsicValue = intrinsicValue,
                ),
            )
        ).getOrThrow()
    }
}
