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
        val eps: Double?,
        val pe: Double?,
        val pb: Double?,
        val earningsQuarterlyGrowth: Double?,
        val expectedEpsGrowth: Double?,
        val valuationFloor: Double?,
        val currentIntrinsicValue: Double?,
        val forwardIntrinsicValue: Double?,
        val currency: String?,
        val lastUpdated: Long,
        val isWatchlisted: Boolean,
    ) {

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
