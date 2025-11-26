package org.n27.ktstonks.data.db.mappers

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.n27.ktstonks.data.db.tables.StockTable
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

internal fun Query.toStocks(page: Int, pageSize: Int) = run {
    val stocks = count()
    val hasNextPage = (page + 1) * pageSize < stocks
    val offset = (page * pageSize).toLong()
    val nextPageValue = if (hasNextPage) page + 1 else null
    Stocks(
        items = limit(pageSize, offset).map { it.toStock() },
        nextPage = nextPageValue
    )
}

internal fun ResultRow.toStock() = Stock(
    symbol = this[StockTable.symbol],
    companyName = this[StockTable.companyName],
    logoUrl = this[StockTable.logoUrl],
    price = this[StockTable.price],
    dividendYield = this[StockTable.dividendYield],
    eps = this[StockTable.eps],
    pe = this[StockTable.pe],
    earningsQuarterlyGrowth = this[StockTable.earningsQuarterlyGrowth],
    expectedEpsGrowth = this[StockTable.expectedEpsGrowth],
    currentIntrinsicValue = this[StockTable.currentIntrinsicValue],
    forwardIntrinsicValue = this[StockTable.forwardIntrinsicValue],
    currency = this[StockTable.currency],
    lastUpdated = this[StockTable.lastUpdated],
    isWatchlisted = this[StockTable.isWatchlisted],
)
