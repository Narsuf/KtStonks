package org.n27.ktstonks.data.yfinance.mapping

import org.n27.ktstonks.data.yfinance.model.StockRaw
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock

internal fun StockRaw.toDomainEntity(logo: String? = null) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividendYield = dividendYield,
    roe = roe,
    profitMargin = profitMargin,
    incomeStatement = Stocks.IncomeStatement(
        eps = eps,
        earningsQuarterlyGrowth = earningsQuarterlyGrowth,
        revenueQuarterlyGrowth = revenueQuarterlyGrowth,
    ),
    analysis = Stocks.Analysis(
        earningsEstimate = Stocks.Analysis.Estimate(earningsEstimateGrowthLow, earningsEstimateGrowthHigh),
        revenueEstimate = Stocks.Analysis.Estimate(revenueEstimateGrowthLow, revenueEstimateGrowthHigh),
    ),
    valuationMeasures = Stocks.ValuationMeasures(
        pe = pe,
        valuationFloor = null,
        intrinsicValue = null,
    ),
    balanceSheet = Stocks.BalanceSheet(
        totalCashPerShare = totalCashPerShare,
        de = de,
    ),
    currency = currency,
    lastUpdated = System.currentTimeMillis(),
    isWatchlisted = false,
)
