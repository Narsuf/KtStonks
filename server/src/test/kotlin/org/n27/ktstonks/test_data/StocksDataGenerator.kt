package org.n27.ktstonks.test_data

import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock

fun getStock(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String? = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB",
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
    lastUpdated: Long = 0L
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
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
    lastUpdated = lastUpdated
)

fun getStocks(
    items: List<Stock> = listOf(getStock()),
    nextPage: Int? = 1,
) = Stocks(
    items = items,
    nextPage = nextPage,
)
