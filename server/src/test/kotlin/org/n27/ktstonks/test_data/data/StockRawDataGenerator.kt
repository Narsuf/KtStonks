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
    revenueQuarterlyGrowth: Double? = 5.2,
    revenueEstimateGrowthLow: Double? = 5.60331523810161,
    revenueEstimateGrowthHigh: Double? = 10.2768231268171,
    earningsEstimateGrowthLow: Double? = 2.57668711656441,
    earningsEstimateGrowthHigh: Double? = 15.7190635451505,
    currency: String = "USD",
) = StockRaw(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    revenueQuarterlyGrowth = revenueQuarterlyGrowth,
    revenueEstimateGrowthLow = revenueEstimateGrowthLow,
    revenueEstimateGrowthHigh = revenueEstimateGrowthHigh,
    earningsEstimateGrowthLow = earningsEstimateGrowthLow,
    earningsEstimateGrowthHigh = earningsEstimateGrowthHigh,
    currency = currency,
)

fun getStocksRaw() = listOf(getStockRaw())
