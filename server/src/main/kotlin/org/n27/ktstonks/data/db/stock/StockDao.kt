package org.n27.ktstonks.data.db.stock

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

class StockDao {

    suspend fun getStocks(symbols: Collection<String>): List<Stock> = dbQuery {
        StocksTable
            .select { StocksTable.symbol inList symbols }
            .map { it.toStock() }
    }

    suspend fun getStock(symbol: String): Stock? = dbQuery {
        StocksTable
            .select { StocksTable.symbol eq symbol }
            .map { it.toStock() }
            .singleOrNull()
    }

    suspend fun saveStock(stock: Stock) {
        dbQuery {
            val existingStock = getStock(stock.symbol)

            if (existingStock != null) {
                StocksTable.update(where = { StocksTable.symbol eq stock.symbol }) {
                    it.fromStock(stock.copy(isWatchlisted = existingStock.isWatchlisted))
                }
            } else {
                StocksTable.insert {
                    it[symbol] = stock.symbol
                    it.fromStock(stock)
                }
            }
        }
    }

    suspend fun saveStocks(stocks: List<Stock>) {
        if (stocks.isEmpty()) return

        dbQuery {
            StocksTable.batchUpsert(stocks) { stock ->
                this[StocksTable.symbol] = stock.symbol
                fromStock(stock)
            }
        }
    }

    suspend fun addToWatchlist(symbol: String) {
        dbQuery {
            StocksTable.update(where = { StocksTable.symbol eq symbol }) { it[isWatchlisted] = true }
        }
    }

    suspend fun getWatchlist(page: Int, pageSize: Int): Stocks = dbQuery {
        StocksTable
            .select { StocksTable.isWatchlisted eq true }
            .toStocks(page, pageSize)
    }

    suspend fun removeFromWatchlist(symbol: String) {
        dbQuery {
            StocksTable.update(where = { StocksTable.symbol eq symbol }) { it[isWatchlisted] = false }
        }
    }

    private fun <T> UpdateBuilder<T>.fromStock(stock: Stock) {
        this[StocksTable.companyName] = stock.companyName
        this[StocksTable.logoUrl] = stock.logoUrl
        this[StocksTable.price] = stock.price
        this[StocksTable.dividendYield] = stock.dividendYield
        this[StocksTable.eps] = stock.eps
        this[StocksTable.pe] = stock.pe
        this[StocksTable.pb] = stock.pb
        this[StocksTable.earningsQuarterlyGrowth] = stock.earningsQuarterlyGrowth
        this[StocksTable.expectedEpsGrowth] = stock.expectedEpsGrowth
        this[StocksTable.valuationFloor] = stock.valuationFloor
        this[StocksTable.currentIntrinsicValue] = stock.currentIntrinsicValue
        this[StocksTable.forwardIntrinsicValue] = stock.forwardIntrinsicValue
        this[StocksTable.currency] = stock.currency
        this[StocksTable.lastUpdated] = stock.lastUpdated
        this[StocksTable.isWatchlisted] = stock.isWatchlisted
    }

    private fun Query.toStocks(page: Int, pageSize: Int) = run {
        val stocks = count()
        val hasNextPage = (page + 1) * pageSize < stocks
        val offset = (page * pageSize).toLong()
        val nextPageValue = if (hasNextPage) page + 1 else null
        Stocks(
            items = limit(pageSize, offset).map { it.toStock() },
            nextPage = nextPageValue
        )
    }

    private fun ResultRow.toStock() = Stock(
        symbol = this[StocksTable.symbol],
        companyName = this[StocksTable.companyName],
        logoUrl = this[StocksTable.logoUrl],
        price = this[StocksTable.price],
        dividendYield = this[StocksTable.dividendYield],
        eps = this[StocksTable.eps],
        pe = this[StocksTable.pe],
        pb = this[StocksTable.pb],
        earningsQuarterlyGrowth = this[StocksTable.earningsQuarterlyGrowth],
        expectedEpsGrowth = this[StocksTable.expectedEpsGrowth],
        valuationFloor = this[StocksTable.valuationFloor],
        currentIntrinsicValue = this[StocksTable.currentIntrinsicValue],
        forwardIntrinsicValue = this[StocksTable.forwardIntrinsicValue],
        currency = this[StocksTable.currency],
        lastUpdated = this[StocksTable.lastUpdated],
        isWatchlisted = this[StocksTable.isWatchlisted],
    )
}
