package org.n27.ktstonks.data.yfinance.mapping

import org.n27.ktstonks.data.yfinance.model.StockRaw
import org.n27.ktstonks.domain.mapping.mapToStock

internal fun StockRaw.toDomainEntity(logo: String? = null) = mapToStock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    currency = currency,
    lastUpdated = System.currentTimeMillis(),
    isWatchlisted = false,
    dividendYield = dividendYield,
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    growthHigh = earningsEstimateGrowthHigh,
    pe = pe,
    valuationFloor = null,
    intrinsicValue = null,
    de = de,
    totalCashPerShare = totalCashPerShare,
    roe = roe,
    profitMargin = profitMargin,
)
