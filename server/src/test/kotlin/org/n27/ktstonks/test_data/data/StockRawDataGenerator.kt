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
    pb: Double = 51.967537,
    ps: Double? = 8.78231,
    earningsQuarterlyGrowth: Double = 86.4,
    revenueQuarterlyGrowth: Double? = 5.2,
    revenueEstimateGrowthLow: Double? = null,
    revenueEstimateGrowthHigh: Double? = null,
    earningsEstimateGrowthLow: Double? = null,
    earningsEstimateGrowthHigh: Double? = null,
    intrinsicValue: Double = 119.52,
    currency: String = "USD",
) = StockRaw(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    pb = pb,
    ps = ps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    revenueQuarterlyGrowth = revenueQuarterlyGrowth,
    revenueEstimateGrowthLow = revenueEstimateGrowthLow,
    revenueEstimateGrowthHigh = revenueEstimateGrowthHigh,
    earningsEstimateGrowthLow = earningsEstimateGrowthLow,
    earningsEstimateGrowthHigh = earningsEstimateGrowthHigh,
    intrinsicValue = intrinsicValue,
    currency = currency,
)

fun getStocksRaw() = listOf(getStockRaw())
