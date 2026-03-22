package org.n27.ktstonks.data.db.stocks

import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.*
import java.util.*

fun StocksEntity.toStocks() = Stocks(
    items = items.map { it.toStock() },
    nextPage = nextPage
)

fun StockEntity.toStock() = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.bytes?.let { Base64.getEncoder().encodeToString(it) },
    price = price,
    dividendYield = dividendYield,
    incomeStatement = incomeStatement?.let {
        IncomeStatement(
            eps = it.eps,
            earningsQuarterlyGrowth = it.earningsQuarterlyGrowth,
            revenueQuarterlyGrowth = it.revenueQuarterlyGrowth,
        )
    },
    analysis = analysis?.let {
        Analysis(
            earningsEstimate = it.earningsEstimate?.let { e ->
                Analysis.Estimate(growthLow = e.growthLow, growthHigh = e.growthHigh)
            },
            revenueEstimate = it.revenueEstimate?.let { e ->
                Analysis.Estimate(growthLow = e.growthLow, growthHigh = e.growthHigh)
            },
        )
    },
    valuationMeasures = ValuationMeasures(
        pe = valuationMeasures?.pe,
        pb = valuationMeasures?.pb,
        ps = valuationMeasures?.ps,
        valuationFloor = valuationMeasures?.valuationFloor,
        intrinsicValue = valuationMeasures?.intrinsicValue,
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun Stock.toEntity() = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.let { StockEntity.Logo(Base64.getDecoder().decode(it)) },
    price = price,
    dividendYield = dividendYield,
    incomeStatement = incomeStatement?.let {
        StockEntity.IncomeStatement(
            eps = it.eps,
            earningsQuarterlyGrowth = it.earningsQuarterlyGrowth,
            revenueQuarterlyGrowth = it.revenueQuarterlyGrowth,
        )
    },
    analysis = analysis?.let {
        StockEntity.Analysis(
            earningsEstimate = it.earningsEstimate?.let { e ->
                StockEntity.Analysis.Estimate(growthLow = e.growthLow, growthHigh = e.growthHigh)
            },
            revenueEstimate = it.revenueEstimate?.let { e ->
                StockEntity.Analysis.Estimate(growthLow = e.growthLow, growthHigh = e.growthHigh)
            },
        )
    },
    valuationMeasures = StockEntity.ValuationMeasures(
        pe = valuationMeasures.pe,
        pb = valuationMeasures.pb,
        ps = valuationMeasures.ps,
        valuationFloor = valuationMeasures.valuationFloor,
        intrinsicValue = valuationMeasures.intrinsicValue,
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)
