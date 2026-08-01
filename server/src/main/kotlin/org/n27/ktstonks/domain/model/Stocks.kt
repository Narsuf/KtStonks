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
        val roe: RatedValue?,
        val profitMargin: RatedValue?,
        val incomeStatement: IncomeStatement,
        val earningsEstimate: RatedValue?,
        val valuationMeasures: ValuationMeasures,
        val balanceSheet: BalanceSheet,
        val computed: Computed?,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    )

    @Serializable
    data class Dividends(
        val dividendYield: Double?,
        val payoutRatio: RatedValue?,
    )

    @Serializable
    data class Computed(
        val earningsYield: Double?,
        val peg: RatedValue?,
        val dynamicPayback: RatedValue?,
    )

    @Serializable
    data class IncomeStatement(
        val eps: Double?,
        val earningsQuarterlyGrowth: Double?,
    )

    @Serializable
    data class ValuationMeasures(
        val pe: RatedValue?,
        val valuationFloor: Double?,
        val intrinsicValue: Double?,
    )

    @Serializable
    data class BalanceSheet(
        val totalCashPerShare: Double?,
        val de: RatedValue?,
    )
}
