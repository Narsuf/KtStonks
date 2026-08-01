package org.n27.ktstonks.data.db.stocks

import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.Logo
import org.n27.ktstonks.domain.mapping.mapToStock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock
import java.util.*

fun StocksEntity.toStocks() = Stocks(
    items = items.map { it.toStock() },
    nextPage = nextPage
)

fun StockEntity.toStock() = mapToStock(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.bytes?.let { Base64.getEncoder().encodeToString(it) },
    price = price,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    dividendYield = dividends.dividendYield,
    eps = incomeStatement.eps,
    earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth,
    growthHigh = earningsEstimate.growthHigh,
    pe = valuationMeasures.pe,
    valuationFloor = valuationMeasures.valuationFloor,
    intrinsicValue = valuationMeasures.intrinsicValue,
    de = balanceSheet.de,
    totalCashPerShare = balanceSheet.totalCashPerShare,
    roe = roe,
    profitMargin = profitMargin,
)

fun Stock.toEntity() = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.let { Logo(Base64.getDecoder().decode(it)) },
    price = price,
    dividends = StockEntity.Dividends(
        dividendYield = dividends.dividendYield,
    ),
    roe = roe?.value,
    profitMargin = profitMargin?.value,
    incomeStatement = StockEntity.IncomeStatement(
        eps = incomeStatement.eps,
        earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth,
    ),
    earningsEstimate = StockEntity.Estimate(
        growthHigh = earningsEstimate?.value,
    ),
    valuationMeasures = StockEntity.ValuationMeasures(
        pe = valuationMeasures.pe?.value,
        valuationFloor = valuationMeasures.valuationFloor,
        intrinsicValue = valuationMeasures.intrinsicValue,
    ),
    balanceSheet = StockEntity.BalanceSheet(
        totalCashPerShare = balanceSheet.totalCashPerShare,
        de = balanceSheet.de?.value,
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)
