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
    incomeStatement: IncomeStatement = getStockEntityIncomeStatement(),
    analysis: Analysis = getStockEntityAnalysis(),
    valuationMeasures: ValuationMeasures = getStockEntityValuationMeasures(),
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

fun getStockEntityIncomeStatement() = IncomeStatement(
    eps = 7.47,
    earningsQuarterlyGrowth = 86.4,
    revenueQuarterlyGrowth = 5.2,
)

fun getStockEntityAnalysis() = Analysis(
    earningsEstimate = getStockEntityAnalysisEstimate(),
    revenueEstimate = getStockEntityAnalysisEstimate(
        growthLow = 5.60331523810161,
        growthHigh = 10.2768231268171,
    ),
)

fun getStockEntityAnalysisEstimate(
    growthLow: Double = 2.57668711656441,
    growthHigh: Double = 15.7190635451505,
) = Analysis.Estimate(
    growthLow = growthLow,
    growthHigh = growthHigh,
)

fun getStockEntityValuationMeasures() = ValuationMeasures(
    pe = 34.7215522245231,
    pb = 51.967537,
    ps = 8.78231,
    valuationFloor = null,
    intrinsicValue = null,
)
