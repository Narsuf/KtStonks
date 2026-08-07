package org.n27.ktstonks.test_data.data

import org.n27.ktstonks.data.db.stocks.StocksEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.*
import java.util.*

fun getStockEntity(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB",
    price: Double? = 259.369995117188,
    dividends: Dividends = getStockEntityDividends(),
    roe: Metric = Metric(value = 1.5202099, variation = null),
    profitMargin: Metric = Metric(value = 0.27037, variation = null),
    incomeStatement: IncomeStatement = getStockEntityIncomeStatement(),
    earningsEstimate: Estimate = getStockEntityEarningsEstimate(),
    valuationMeasures: ValuationMeasures = getStockEntityValuationMeasures(),
    balanceSheet: BalanceSheet = getStockEntityBalanceSheet(),
    currency: String = "USD",
    lastUpdated: Long = 0L,
    isWatchlisted: Boolean = false
) = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = Logo(Base64.getDecoder().decode(logo)),
    price = price,
    dividends = dividends,
    roe = roe,
    profitMargin = profitMargin,
    incomeStatement = incomeStatement,
    earningsEstimate = earningsEstimate,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
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

fun getStockEntityIncomeStatement(
    eps: Metric = Metric(value = 7.47, variation = null),
    earningsQuarterlyGrowth: Metric = Metric(value = 86.4, variation = null),
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getStockEntityEarningsEstimate(
    growthHigh: Metric = Metric(value = 15.7190635451505, variation = null),
) = Estimate(
    growthHigh = growthHigh,
)

fun getStockEntityValuationMeasures(
    pe: Metric = Metric(value = 34.7215522245231, variation = null),
    valuationFloor: Double? = null,
    intrinsicValue: Double? = null,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getStockEntityDividends(
    dividendYield: Metric = Metric(value = 0.4, variation = null),
) = Dividends(
    dividendYield = dividendYield,
)

fun getStockEntityBalanceSheet(
    totalCashPerShare: Metric = Metric(value = 4.557, variation = null),
    de: Metric = Metric(value = 102.63, variation = null),
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
)
