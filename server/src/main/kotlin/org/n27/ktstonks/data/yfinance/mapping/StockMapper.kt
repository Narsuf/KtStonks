package org.n27.ktstonks.data.yfinance.mapping

import org.n27.ktstonks.data.yfinance.model.StockRaw
import org.n27.ktstonks.domain.model.Stocks.*

internal fun StockRaw.toDomainEntity(logo: String? = null) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = Dividends(
        dividendYield = dividendYield,
        payoutRatio = payoutRatio,
    ),
    roe = roe,
    profitMargin = profitMargin,
    incomeStatement = IncomeStatement(
        eps = eps,
        earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    ),
    earningsEstimate = Estimate(
        growthHigh = earningsEstimateGrowthHigh,
        growthAvg = earningsEstimateGrowthAvg,
    ),
    valuationMeasures = ValuationMeasures(
        pe = pe,
        valuationFloor = null,
        intrinsicValue = null,
    ),
    balanceSheet = BalanceSheet(
        totalCashPerShare = totalCashPerShare,
        de = de,
        currentRatio = currentRatio,
    ),
    currency = currency,
    lastUpdated = System.currentTimeMillis(),
    isWatchlisted = false,
)
