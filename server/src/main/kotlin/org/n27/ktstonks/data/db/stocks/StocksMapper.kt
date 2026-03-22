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
    incomeStatement = IncomeStatement(
        eps = incomeStatement.eps,
        earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth,
        revenueQuarterlyGrowth = incomeStatement.revenueQuarterlyGrowth,
    ),
    analysis = Analysis(
        earningsEstimate = Analysis.Estimate(
            growthLow = analysis.earningsEstimate.growthLow,
            growthHigh = analysis.earningsEstimate.growthHigh,
        ),
        revenueEstimate = Analysis.Estimate(
            growthLow = analysis.revenueEstimate.growthLow,
            growthHigh = analysis.revenueEstimate.growthHigh,
        ),
    ),
    valuationMeasures = ValuationMeasures(
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

fun Stock.toEntity() = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.let { StockEntity.Logo(Base64.getDecoder().decode(it)) },
    price = price,
    dividendYield = dividendYield,
    incomeStatement = StockEntity.IncomeStatement(
        eps = incomeStatement.eps,
        earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth,
        revenueQuarterlyGrowth = incomeStatement.revenueQuarterlyGrowth,
    ),
    analysis = StockEntity.Analysis(
        earningsEstimate = StockEntity.Analysis.Estimate(
            growthLow = analysis.earningsEstimate.growthLow,
            growthHigh = analysis.earningsEstimate.growthHigh,
        ),
        revenueEstimate = StockEntity.Analysis.Estimate(
            growthLow = analysis.revenueEstimate.growthLow,
            growthHigh = analysis.revenueEstimate.growthHigh,
        ),
    ),
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
