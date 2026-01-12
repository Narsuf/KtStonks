package org.n27.ktstonks.test_data.data

import org.n27.ktstonks.data.db.stocks.StocksEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import java.util.*

fun getStockEntity(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String = "aHR0cHM6Ly9pbWcubG9nby5kZXYvYXBwbGUuY29t",
    price: Double = 259.369995117188,
    dividendYield: Double = 0.4,
    eps: Double = 7.47,
    pe: Double = 34.7215522245231,
    pb: Double = 51.967537,
    earningsQuarterlyGrowth: Double = 86.4,
    expectedEpsGrowth: Double? = null,
    valuationFloor: Double? = null,
    currentIntrinsicValue: Double = 119.52,
    forwardIntrinsicValue: Double? = null,
    currency: String = "USD",
    lastUpdated: Long = 0L,
    isWatchlisted: Boolean = false
) = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = StockEntity.Logo(Base64.getDecoder().decode(logo)),
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    pb = pb,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = expectedEpsGrowth,
    valuationFloor = valuationFloor,
    currentIntrinsicValue = currentIntrinsicValue,
    forwardIntrinsicValue = forwardIntrinsicValue,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted
)

fun getStocksEntity(
    items: List<StockEntity> = listOf(getStockEntity()),
    nextPage: Int = 1,
) = StocksEntity(
    items = items,
    nextPage = nextPage,
)
