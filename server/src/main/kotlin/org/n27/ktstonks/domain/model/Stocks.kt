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
        val roe: MetricValue?,
        val profitMargin: MetricValue?,
        val incomeStatement: IncomeStatement,
        val earningsEstimate: MetricValue?,
        val valuationMeasures: ValuationMeasures,
        val balanceSheet: BalanceSheet,
        val computed: Computed?,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    )

    @Serializable
    data class Dividends(
        val dividendYield: MetricValue?,
        val payoutRatio: MetricValue?,
    )

    @Serializable
    data class Computed(
        val earningsYield: Double?,
        val peg: MetricValue?,
        val dynamicPayback: MetricValue?,
    )

    @Serializable
    data class IncomeStatement(
        val eps: MetricValue?,
        val earningsQuarterlyGrowth: MetricValue?,
    )

    @Serializable
    data class ValuationMeasures(
        val pe: MetricValue?,
        val valuationFloor: Double?,
        val intrinsicValue: Double?,
    )

    @Serializable
    data class BalanceSheet(
        val totalCashPerShare: MetricValue?,
        val de: MetricValue?,
    )
}
