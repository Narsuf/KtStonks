package org.n27.ktstonks.test_data

import org.n27.ktstonks.domain.mapping.StockRatingMapper
import org.n27.ktstonks.domain.model.MetricValue
import org.n27.ktstonks.domain.model.Rating
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
    price: Double? = 259.369995117188,
    dividends: Dividends = getStockDividends(),
    roe: MetricValue? = MetricValue(1.5202099, Rating.CAUTION),
    profitMargin: MetricValue? = MetricValue(0.27037, Rating.CAUTION),
    incomeStatement: IncomeStatement = getStockIncomeStatement(),
    earningsEstimate: MetricValue? = MetricValue(15.7190635451505, Rating.CAUTION),
    valuationMeasures: ValuationMeasures = getStockValuationMeasures(),
    balanceSheet: BalanceSheet = getStockBalanceSheet(),
    computed: Computed? = getStockComputed(),
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
    earningsEstimate = earningsEstimate,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
    computed = computed,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun getStockIncomeStatement(
    eps: MetricValue? = MetricValue(7.47, null),
    earningsQuarterlyGrowth: MetricValue? = MetricValue(86.4, null),
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getStockValuationMeasures(
    pe: MetricValue? = MetricValue(34.7215522245231, Rating.WARNING),
    valuationFloor: Double? = null,
    intrinsicValue: Double? = null,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getStockDividends(
    dividendYield: MetricValue? = MetricValue(0.4, null),
    payoutRatio: MetricValue? = MetricValue(13.888620889809241, null),
) = Dividends(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getStockBalanceSheet(
    totalCashPerShare: MetricValue? = MetricValue(4.557, null),
    de: MetricValue? = MetricValue(102.63, Rating.DANGER),
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
)

fun getStockComputed(
    earningsYield: Double? = 2.880055573361496,
    peg: MetricValue? = MetricValue(2.208881726623917, Rating.CAUTION),
    dynamicPayback: MetricValue? = MetricValue(12.776479013085757, null),
) = Computed(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
)
