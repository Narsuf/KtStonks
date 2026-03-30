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
        val roe: Double?,
        val profitMargin: Double?,
        val incomeStatement: IncomeStatement,
        val earningsEstimate: Estimate,
        val valuationMeasures: ValuationMeasures,
        val balanceSheet: BalanceSheet,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    ) {

        data class Dividends(
            val dividendYield: Double?,
            val payoutRatio: Double?,
        )

        data class IncomeStatement(
            val eps: Double?,
            val earningsQuarterlyGrowth: Double?,
        )

        data class ValuationMeasures(
            val pe: Double?,
            val valuationFloor: Double?,
            val intrinsicValue: Double?,
        )

        data class BalanceSheet(
            val totalCashPerShare: Double?,
            val de: Double?,
            val currentRatio: Double?,
        )

        data class Estimate(
            val growthHigh: Double?,
            val growthAvg: Double?,
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
