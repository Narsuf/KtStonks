package org.n27.ktstonks.data.db.stocks

import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.Logo
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
    dividends = Dividends(
        dividendYield = dividends.dividendYield,
        payoutRatio = dividends.payoutRatio,
    ),
    roe = roe,
    profitMargin = profitMargin,
    incomeStatement = IncomeStatement(
        eps = incomeStatement.eps,
        earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth,
    ),
    earningsEstimate = Estimate(
        growthHigh = earningsEstimate.growthHigh,
        growthAvg = earningsEstimate.growthAvg,
    ),
    valuationMeasures = ValuationMeasures(
        pe = valuationMeasures.pe,
        valuationFloor = valuationMeasures.valuationFloor,
        intrinsicValue = valuationMeasures.intrinsicValue,
    ),
    balanceSheet = BalanceSheet(
        totalCashPerShare = balanceSheet.totalCashPerShare,
        de = balanceSheet.de,
        currentRatio = balanceSheet.currentRatio,
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun Stock.toEntity() = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.let { Logo(Base64.getDecoder().decode(it)) },
    price = price,
    dividends = StockEntity.Dividends(
        dividendYield = dividends.dividendYield,
        payoutRatio = dividends.payoutRatio,
    ),
    roe = roe,
    profitMargin = profitMargin,
    incomeStatement = StockEntity.IncomeStatement(
        eps = incomeStatement.eps,
        earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth,
    ),
    earningsEstimate = StockEntity.Estimate(
        growthHigh = earningsEstimate.growthHigh,
        growthAvg = earningsEstimate.growthAvg,
    ),
    valuationMeasures = StockEntity.ValuationMeasures(
        pe = valuationMeasures.pe,
        valuationFloor = valuationMeasures.valuationFloor,
        intrinsicValue = valuationMeasures.intrinsicValue,
    ),
    balanceSheet = StockEntity.BalanceSheet(
        totalCashPerShare = balanceSheet.totalCashPerShare,
        de = balanceSheet.de,
        currentRatio = balanceSheet.currentRatio,
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)
