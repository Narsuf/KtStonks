package org.n27.ktstonks.data.yfinance.mapping

import org.n27.ktstonks.data.yfinance.model.StockRaw
import org.n27.ktstonks.domain.model.Stocks.*
import org.n27.ktstonks.domain.model.Stocks.Analysis.Estimate

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
        revenueQuarterlyGrowth = revenueQuarterlyGrowth,
    ),
    analysis = Analysis(
        earningsEstimate = Estimate(earningsEstimateGrowthLow, earningsEstimateGrowthHigh),
        revenueEstimate = Estimate(revenueEstimateGrowthLow, revenueEstimateGrowthHigh),
    ),
    valuationMeasures = ValuationMeasures(
        pe = pe,
        valuationFloor = null,
        intrinsicValue = null,
    ),
    balanceSheet = BalanceSheet(
        totalCashPerShare = totalCashPerShare,
        de = de,
    ),
    currency = currency,
    lastUpdated = System.currentTimeMillis(),
    isWatchlisted = false,
)
