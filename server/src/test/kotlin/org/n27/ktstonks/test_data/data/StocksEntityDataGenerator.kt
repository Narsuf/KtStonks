package org.n27.ktstonks.test_data.data

import org.n27.ktstonks.data.db.stocks.StocksEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.*
import java.util.*

fun getStockEntity(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB",
    price: Double = 259.369995117188,
    dividendYield: Double = 0.4,
    incomeStatement: IncomeStatement = IncomeStatement(
        eps = 7.47,
        earningsQuarterlyGrowth = 86.4,
        revenueQuarterlyGrowth = 5.2,
    ),
    analysis: Analysis = Analysis(
        earningsEstimate = Analysis.Estimate(null, null),
        revenueEstimate = Analysis.Estimate(null, null),
    ),
    valuationMeasures: ValuationMeasures = ValuationMeasures(
        pe = 34.7215522245231,
        pb = 51.967537,
        ps = 8.78231,
        valuationFloor = null,
        intrinsicValue = null,
    ),
    currency: String = "USD",
    lastUpdated: Long = 0L,
    isWatchlisted: Boolean = false
) = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = Logo(Base64.getDecoder().decode(logo)),
    price = price,
    dividendYield = dividendYield,
    incomeStatement = incomeStatement,
    analysis = analysis,
    valuationMeasures = valuationMeasures,
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
