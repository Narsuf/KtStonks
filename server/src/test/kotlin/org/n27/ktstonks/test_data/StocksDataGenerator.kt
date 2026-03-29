package org.n27.ktstonks.test_data

import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.*
import org.n27.ktstonks.domain.model.Stocks.Analysis.Estimate

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
    dividends: Dividends = getStockDividends(),
    roe: Double? = 1.5202099,
    profitMargin: Double? = 0.27037,
    incomeStatement: IncomeStatement = getStockIncomeStatement(),
    analysis: Analysis = getStockAnalysis(),
    valuationMeasures: ValuationMeasures = getStockValuationMeasures(),
    balanceSheet: BalanceSheet = getStockBalanceSheet(),
    currency: String = "USD",
    lastUpdated: Long = 0L,
    isWatchlisted: Boolean = false,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = dividends,
    roe = roe,
    profitMargin = profitMargin,
    incomeStatement = incomeStatement,
    analysis = analysis,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun getStockIncomeStatement(
    eps: Double = 7.47,
    earningsQuarterlyGrowth: Double = 86.4,
    revenueQuarterlyGrowth: Double = 5.2,
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    revenueQuarterlyGrowth = revenueQuarterlyGrowth,
)

fun getStockAnalysis(
    earningsEstimate: Estimate = getStockAnalysisEstimate(),
    revenueEstimate: Estimate = getStockAnalysisEstimate(
        growthLow = 5.60331523810161,
        growthHigh = 10.2768231268171,
        growthAvg = 5.53740676977713,
    ),
) = Analysis(
    earningsEstimate = earningsEstimate,
    revenueEstimate = revenueEstimate,
)

fun getStockAnalysisEstimate(
    growthLow: Double = 2.57668711656441,
    growthHigh: Double = 15.7190635451505,
    growthAvg: Double = 8.65392250039098,
) = Estimate(
    growthLow = growthLow,
    growthHigh = growthHigh,
    growthAvg = growthAvg,
)

fun getStockValuationMeasures(
    pe: Double = 34.7215522245231,
    valuationFloor: Double? = null,
    intrinsicValue: Double? = null,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getStockDividends(
    dividendYield: Double = 0.4,
    payoutRatio: Double = 0.1476,
) = Dividends(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getStockBalanceSheet(
    totalCashPerShare: Double = 4.557,
    de: Double = 102.63,
    currentRatio: Double? = 1.5,
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
    currentRatio = currentRatio,
)
