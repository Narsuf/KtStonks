package org.n27.ktstonks.test_data

import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.*

fun getStocks(
    items: List<Stock> = listOf(getStock()),
    nextPage: Int? = 1,
) = Stocks(
    items = items,
    nextPage = nextPage,
)

fun getStock(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String? = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB",
    price: Double = 259.369995117188,
    dividendYield: Double = 0.4,
    incomeStatement: IncomeStatement = getStockIncomeStatement(),
    analysis: Analysis = getStockAnalysis(),
    valuationMeasures: ValuationMeasures = getStockValuationMeasures(),
    currency: String = "USD",
    lastUpdated: Long = 0L,
    isWatchlisted: Boolean = false,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividendYield = dividendYield,
    incomeStatement = incomeStatement,
    analysis = analysis,
    valuationMeasures = valuationMeasures,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun getStockIncomeStatement() = IncomeStatement(
    eps = 7.47,
    earningsQuarterlyGrowth = 86.4,
    revenueQuarterlyGrowth = 5.2,
)

fun getStockAnalysis() = Analysis(
    earningsEstimate = getStockAnalysisEstimate(),
    revenueEstimate = getStockAnalysisEstimate(
        growthLow = 5.60331523810161,
        growthHigh = 10.2768231268171,
    ),
)

fun getStockAnalysisEstimate(
    growthLow: Double = 2.57668711656441,
    growthHigh: Double = 15.7190635451505,
) = Analysis.Estimate(
    growthLow = growthLow,
    growthHigh = growthHigh,
)

fun getStockValuationMeasures() = ValuationMeasures(
    pe = 34.7215522245231,
    valuationFloor = null,
    intrinsicValue = null,
)
