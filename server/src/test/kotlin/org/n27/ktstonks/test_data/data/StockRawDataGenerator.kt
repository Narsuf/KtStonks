package org.n27.ktstonks.test_data.data

import org.n27.ktstonks.data.yfinance.model.StockRaw

fun getStockRaw(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logoUrl: String = "https://img.logo.dev/apple.com",
    price: Double = 259.369995117188,
    dividendYield: Double = 0.4,
    eps: Double = 7.47,
    pe: Double = 34.7215522245231,
    earningsQuarterlyGrowth: Double = 86.4,
    earningsEstimateGrowthHigh: Double? = 15.7190635451505,
    earningsEstimateGrowthAvg: Double? = 8.65392250039098,
    currency: String = "USD",
    roe: Double? = 1.5202099,
    profitMargin: Double? = 0.27037,
    totalCashPerShare: Double? = 4.557,
    de: Double? = 102.63,
    payoutRatio: Double? = 0.1476,
    currentRatio: Double? = 1.5,
) = StockRaw(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    earningsEstimateGrowthHigh = earningsEstimateGrowthHigh,
    earningsEstimateGrowthAvg = earningsEstimateGrowthAvg,
    currency = currency,
    roe = roe,
    profitMargin = profitMargin,
    totalCashPerShare = totalCashPerShare,
    de = de,
    payoutRatio = payoutRatio,
    currentRatio = currentRatio,
)

fun getStocksRaw() = listOf(getStockRaw())
