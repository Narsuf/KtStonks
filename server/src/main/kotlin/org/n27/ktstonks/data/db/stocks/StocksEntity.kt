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
        val dividendYield: Double?,
        val incomeStatement: IncomeStatement?,
        val analysis: Analysis?,
        val valuationMeasures: ValuationMeasures,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    ) {

        data class IncomeStatement(
            val eps: Double?,
            val earningsQuarterlyGrowth: Double?,
            val revenueQuarterlyGrowth: Double?,
        )

        data class ValuationMeasures(
            val pe: Double?,
            val pb: Double?,
            val ps: Double?,
            val valuationFloor: Double?,
            val intrinsicValue: Double?,
        )

        data class Analysis(
            val earningsEstimate: Estimate?,
            val revenueEstimate: Estimate?,
        ) {

            data class Estimate(
                val growthLow: Double?,
                val growthHigh: Double?,
            )
        }

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
