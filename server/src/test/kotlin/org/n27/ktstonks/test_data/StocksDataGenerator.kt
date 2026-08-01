package org.n27.ktstonks.test_data

import org.n27.ktstonks.domain.mapping.StockRatingMapper
import org.n27.ktstonks.domain.model.RatedValue
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
    roe: RatedValue? = RatedValue(1.5202099, Rating.CAUTION),
    profitMargin: RatedValue? = RatedValue(0.27037, Rating.CAUTION),
    incomeStatement: IncomeStatement = getStockIncomeStatement(),
    earningsEstimate: RatedValue? = RatedValue(15.7190635451505, Rating.CAUTION),
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
    eps: Double = 7.47,
    earningsQuarterlyGrowth: Double = 86.4,
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getStockValuationMeasures(
    pe: RatedValue? = RatedValue(34.7215522245231, Rating.WARNING),
    valuationFloor: Double? = null,
    intrinsicValue: Double? = null,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getStockDividends(
    dividendYield: Double = 0.4,
    payoutRatio: RatedValue? = RatedValue(13.888620889809241, null),
) = Dividends(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getStockBalanceSheet(
    totalCashPerShare: Double = 4.557,
    de: RatedValue? = RatedValue(102.63, Rating.DANGER),
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
)

fun getStockComputed(
    earningsYield: Double? = 2.880055573361496,
    peg: RatedValue? = RatedValue(2.208881726623917, Rating.CAUTION),
    dynamicPayback: RatedValue? = RatedValue(12.776479013085757, null),
) = Computed(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
)
