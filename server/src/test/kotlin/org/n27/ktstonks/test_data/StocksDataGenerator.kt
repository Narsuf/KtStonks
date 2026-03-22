package org.n27.ktstonks.test_data

import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.*

fun getStock(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String? = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB",
    price: Double = 259.369995117188,
    dividendYield: Double = 0.4,
    incomeStatement: IncomeStatement? = IncomeStatement(
        eps = 7.47,
        earningsQuarterlyGrowth = 86.4,
        revenueQuarterlyGrowth = 5.2,
    ),
    analysis: Analysis? = null,
    valuationMeasures: ValuationMeasures = ValuationMeasures(
        pe = 34.7215522245231,
        pb = 51.967537,
        ps = 8.78231,
        valuationFloor = null,
        intrinsicValue = null,
    ),
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

fun getStocks(
    items: List<Stock> = listOf(getStock()),
    nextPage: Int? = 1,
) = Stocks(
    items = items,
    nextPage = nextPage,
)
