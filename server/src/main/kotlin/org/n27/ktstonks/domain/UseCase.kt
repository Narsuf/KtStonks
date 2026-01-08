package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock

class UseCase(private val repository: Repository) {

    suspend fun addCustomValuation(
        symbol: String,
        valuationFloor: Double?,
        epsGrowth: Double,
    ): Result<Unit> = repository.getStock(symbol).mapCatching {
        val updatedStock = it.recalculateIntrinsicValues(epsGrowth, valuationFloor)
        repository.updateStock(updatedStock).getOrThrow()
    }

    private fun Stock.recalculateIntrinsicValues(growth: Double, valuationFloor: Double?): Stock {
        var returnValue = copy(
            expectedEpsGrowth = growth,
            forwardIntrinsicValue = eps?.getIntrinsicValue(valuationFloor ?: 16.0, growth),
        )

        if (eps != null && valuationFloor != null) {
            returnValue = returnValue.copy(
                valuationFloor = valuationFloor,
                currentIntrinsicValue = eps.getIntrinsicValue(valuationFloor),
            )
        }

        return returnValue
    }

    private fun Double.getIntrinsicValue(
        valuationFloor: Double,
        expectedEpsGrowth: Double = 0.0,
    ) = expectedEpsGrowth.toMultiplier() * valuationFloor * this

    private fun Double.toMultiplier(): Double = 1 + this / 100
}
