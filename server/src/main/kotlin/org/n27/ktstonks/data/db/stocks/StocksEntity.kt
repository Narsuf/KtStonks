package org.n27.ktstonks.data.db.stocks

data class StocksEntity(
    val items: List<StockEntity>,
    val nextPage: Int?
) {

    data class StockEntity(
        val symbol: String,
        val companyName: String,
        val logo: Logo?,
        val price: Double?,
        val dividends: Dividends,
        val roe: Metric,
        val profitMargin: Metric,
        val incomeStatement: IncomeStatement,
        val earningsEstimate: Estimate,
        val valuationMeasures: ValuationMeasures,
        val balanceSheet: BalanceSheet,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    ) {

        data class Metric(
            val value: Double?,
            val variation: Double?,
        )

        data class Dividends(
            val dividendYield: Metric,
        )

        data class IncomeStatement(
            val eps: Metric,
            val earningsQuarterlyGrowth: Metric,
        )

        data class ValuationMeasures(
            val pe: Metric,
            val valuationFloor: Double?,
            val intrinsicValue: Double?,
        )

        data class BalanceSheet(
            val totalCashPerShare: Metric,
            val de: Metric,
        )

        data class Estimate(
            val growthHigh: Metric,
        )

        data class Logo(val bytes: ByteArray) {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return bytes.contentEquals((other as Logo).bytes)
            }

            override fun hashCode(): Int = bytes.contentHashCode()
        }
    }
}
