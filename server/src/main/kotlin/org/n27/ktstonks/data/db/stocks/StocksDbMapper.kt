package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.Logo
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.Metric

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
        dividendYield = Metric(value = this[StocksTable.dividendYield], variation = this[StocksTable.dividendYieldVariation]),
    ),
    roe = Metric(value = this[StocksTable.roe], variation = this[StocksTable.roeVariation]),
    profitMargin = Metric(value = this[StocksTable.profitMargin], variation = this[StocksTable.profitMarginVariation]),
    incomeStatement = StockEntity.IncomeStatement(
        eps = Metric(value = this[StocksTable.eps], variation = this[StocksTable.epsVariation]),
        earningsQuarterlyGrowth = Metric(
            value = this[StocksTable.earningsQuarterlyGrowth],
            variation = this[StocksTable.earningsQuarterlyGrowthVariation],
        ),
    ),
    earningsEstimate = StockEntity.Estimate(
        growthHigh = Metric(
            value = this[StocksTable.earningsEstimateGrowthHigh],
            variation = this[StocksTable.earningsEstimateGrowthHighVariation],
        ),
    ),
    valuationMeasures = StockEntity.ValuationMeasures(
        pe = Metric(value = this[StocksTable.pe], variation = this[StocksTable.peVariation]),
        valuationFloor = this[StocksTable.valuationFloor],
        intrinsicValue = this[StocksTable.intrinsicValue],
    ),
    balanceSheet = StockEntity.BalanceSheet(
        totalCashPerShare = Metric(
            value = this[StocksTable.totalCashPerShare],
            variation = this[StocksTable.totalCashPerShareVariation],
        ),
        de = Metric(value = this[StocksTable.de], variation = this[StocksTable.deVariation]),
    ),
    currency = this[StocksTable.currency],
    lastUpdated = this[StocksTable.lastUpdated],
    isWatchlisted = this[StocksTable.isWatchlisted],
)

internal fun <T> UpdateBuilder<T>.fromStockEntity(stock: StockEntity) {
    this[StocksTable.companyName] = stock.companyName
    this[StocksTable.logo] = stock.logo?.bytes
    this[StocksTable.price] = stock.price
    this[StocksTable.dividendYield] = stock.dividends.dividendYield.value
    this[StocksTable.dividendYieldVariation] = stock.dividends.dividendYield.variation
    this[StocksTable.eps] = stock.incomeStatement.eps.value
    this[StocksTable.epsVariation] = stock.incomeStatement.eps.variation
    this[StocksTable.pe] = stock.valuationMeasures.pe.value
    this[StocksTable.peVariation] = stock.valuationMeasures.pe.variation
    this[StocksTable.earningsQuarterlyGrowth] = stock.incomeStatement.earningsQuarterlyGrowth.value
    this[StocksTable.earningsQuarterlyGrowthVariation] = stock.incomeStatement.earningsQuarterlyGrowth.variation
    this[StocksTable.earningsEstimateGrowthHigh] = stock.earningsEstimate.growthHigh.value
    this[StocksTable.earningsEstimateGrowthHighVariation] = stock.earningsEstimate.growthHigh.variation
    this[StocksTable.roe] = stock.roe.value
    this[StocksTable.roeVariation] = stock.roe.variation
    this[StocksTable.profitMargin] = stock.profitMargin.value
    this[StocksTable.profitMarginVariation] = stock.profitMargin.variation
    this[StocksTable.valuationFloor] = stock.valuationMeasures.valuationFloor
    this[StocksTable.intrinsicValue] = stock.valuationMeasures.intrinsicValue
    this[StocksTable.totalCashPerShare] = stock.balanceSheet.totalCashPerShare.value
    this[StocksTable.totalCashPerShareVariation] = stock.balanceSheet.totalCashPerShare.variation
    this[StocksTable.de] = stock.balanceSheet.de.value
    this[StocksTable.deVariation] = stock.balanceSheet.de.variation
    this[StocksTable.currency] = stock.currency
    this[StocksTable.lastUpdated] = stock.lastUpdated
    this[StocksTable.isWatchlisted] = stock.isWatchlisted
}
