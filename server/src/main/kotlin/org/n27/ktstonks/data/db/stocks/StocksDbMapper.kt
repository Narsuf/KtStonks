package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.Logo

internal fun Query.toStockEntities(page: Int, pageSize: Int): StocksEntity {
    val total = count()
    val offset = (page * pageSize).toLong()
    val hasNextPage = (page + 1) * pageSize < total
    return StocksEntity(
        items = limit(pageSize, offset).map { it.toStockEntity() },
        nextPage = if (hasNextPage) page + 1 else null,
    )
}

internal fun ResultRow.toStockEntity() = StockEntity(
    symbol = this[StocksTable.symbol],
    companyName = this[StocksTable.companyName],
    logo = this[StocksTable.logo]?.let { Logo(it) },
    price = this[StocksTable.price],
    dividends = StockEntity.Dividends(
        dividendYield = this[StocksTable.dividendYield],
        payoutRatio = this[StocksTable.payoutRatio],
    ),
    roe = this[StocksTable.roe],
    profitMargin = this[StocksTable.profitMargin],
    incomeStatement = StockEntity.IncomeStatement(
        eps = this[StocksTable.eps],
        earningsQuarterlyGrowth = this[StocksTable.earningsQuarterlyGrowth],
    ),
    earningsEstimate = StockEntity.Estimate(
        growthHigh = this[StocksTable.earningsEstimateGrowthHigh],
        growthAvg = this[StocksTable.earningsEstimateGrowthAvg],
    ),
    valuationMeasures = StockEntity.ValuationMeasures(
        pe = this[StocksTable.pe],
        valuationFloor = this[StocksTable.valuationFloor],
        intrinsicValue = this[StocksTable.intrinsicValue],
    ),
    balanceSheet = StockEntity.BalanceSheet(
        totalCashPerShare = this[StocksTable.totalCashPerShare],
        de = this[StocksTable.de],
        currentRatio = this[StocksTable.currentRatio],
    ),
    currency = this[StocksTable.currency],
    lastUpdated = this[StocksTable.lastUpdated],
    isWatchlisted = this[StocksTable.isWatchlisted],
)

internal fun <T> UpdateBuilder<T>.fromStockEntity(stock: StockEntity) {
    this[StocksTable.companyName] = stock.companyName
    this[StocksTable.logo] = stock.logo?.bytes
    this[StocksTable.price] = stock.price
    this[StocksTable.dividendYield] = stock.dividends.dividendYield
    this[StocksTable.payoutRatio] = stock.dividends.payoutRatio
    this[StocksTable.eps] = stock.incomeStatement.eps
    this[StocksTable.earningsQuarterlyGrowth] = stock.incomeStatement.earningsQuarterlyGrowth
    this[StocksTable.pe] = stock.valuationMeasures.pe
    this[StocksTable.earningsEstimateGrowthHigh] = stock.earningsEstimate.growthHigh
    this[StocksTable.earningsEstimateGrowthAvg] = stock.earningsEstimate.growthAvg
    this[StocksTable.valuationFloor] = stock.valuationMeasures.valuationFloor
    this[StocksTable.intrinsicValue] = stock.valuationMeasures.intrinsicValue
    this[StocksTable.roe] = stock.roe
    this[StocksTable.profitMargin] = stock.profitMargin
    this[StocksTable.totalCashPerShare] = stock.balanceSheet.totalCashPerShare
    this[StocksTable.de] = stock.balanceSheet.de
    this[StocksTable.currentRatio] = stock.balanceSheet.currentRatio
    this[StocksTable.currency] = stock.currency
    this[StocksTable.lastUpdated] = stock.lastUpdated
    this[StocksTable.isWatchlisted] = stock.isWatchlisted
}
