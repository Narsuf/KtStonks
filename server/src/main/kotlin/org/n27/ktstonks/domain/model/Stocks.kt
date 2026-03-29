package org.n27.ktstonks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Stocks(
    val items: List<Stock>,
    val nextPage: Int?,
) {

    @Serializable
    data class Stock(
        val symbol: String,
        val companyName: String,
        val logo: String?,
        val price: Double?,
        val dividends: Dividends,
        val roe: Double?,
        val profitMargin: Double?,
        val incomeStatement: IncomeStatement,
        val analysis: Analysis,
        val valuationMeasures: ValuationMeasures,
        val balanceSheet: BalanceSheet,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    )

    @Serializable
    data class Dividends(
        val dividendYield: Double?,
        val payoutRatio: Double?,
    )

    @Serializable
    data class IncomeStatement(
        val eps: Double?,
        val earningsQuarterlyGrowth: Double?,
    )

    @Serializable
    data class ValuationMeasures(
        val pe: Double?,
        val valuationFloor: Double?,
        val intrinsicValue: Double?,
    )

    @Serializable
    data class BalanceSheet(
        val totalCashPerShare: Double?,
        val de: Double?,
        val currentRatio: Double?,
    )

    @Serializable
    data class Analysis(
        val earningsEstimate: Estimate,
    ) {

        @Serializable
        data class Estimate(
            val growthHigh: Double?,
            val growthAvg: Double?,
        )
    }
}
