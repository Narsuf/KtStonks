package org.n27.ktstonks.data.db.stock

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.data.db.mappers.toStock
import org.n27.ktstonks.data.db.mappers.toStocks
import org.n27.ktstonks.data.db.tables.StockTable
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

class StockDao {
    suspend fun getStocks(page: Int, pageSize: Int): Stocks = dbQuery {
        StockTable.selectAll().toStocks(page, pageSize)
    }

    suspend fun searchStocks(query: String, page: Int, pageSize: Int): Stocks = dbQuery {
        StockTable
            .select { (StockTable.symbol like "%$query%") or (StockTable.companyName like "%$query%") }
            .toStocks(page, pageSize)
    }

    suspend fun getDbStock(symbol: String): Stock? = dbQuery {
        StockTable
            .select { StockTable.symbol eq symbol }
            .map { it.toStock() }
            .singleOrNull()
    }

    suspend fun saveStock(stock: Stock) {
        dbQuery {
            val existingStock = StockTable.select { StockTable.symbol eq stock.symbol }.singleOrNull()

            if (existingStock != null) {
                StockTable.update(where = { StockTable.symbol eq stock.symbol }) { it.fromStock(stock) }
            } else {
                StockTable.insert {
                    it[symbol] = stock.symbol
                    it.fromStock(stock)
                }
            }
        }
    }

    suspend fun addToWatchlist(symbol: String) {
        dbQuery {
            StockTable.update(where = { StockTable.symbol eq symbol }) { it[isWatchlisted] = true }
        }
    }

    suspend fun getWatchlist(page: Int, pageSize: Int): Stocks = dbQuery {
        StockTable
            .select { StockTable.isWatchlisted eq true }
            .toStocks(page, pageSize)
    }

    suspend fun removeFromWatchlist(symbol: String) {
        dbQuery {
            StockTable.update(where = { StockTable.symbol eq symbol }) { it[isWatchlisted] = false }
        }
    }

    private fun <T> UpdateBuilder<T>.fromStock(stock: Stock) {
        this[StockTable.companyName] = stock.companyName
        this[StockTable.logoUrl] = stock.logoUrl
        this[StockTable.price] = stock.price
        this[StockTable.dividendYield] = stock.dividendYield
        this[StockTable.eps] = stock.eps
        this[StockTable.pe] = stock.pe
        this[StockTable.earningsQuarterlyGrowth] = stock.earningsQuarterlyGrowth
        this[StockTable.expectedEpsGrowth] = stock.expectedEpsGrowth
        this[StockTable.currentIntrinsicValue] = stock.currentIntrinsicValue
        this[StockTable.forwardIntrinsicValue] = stock.forwardIntrinsicValue
        this[StockTable.currency] = stock.currency
        this[StockTable.lastUpdated] = stock.lastUpdated
        this[StockTable.isWatchlisted] = stock.isWatchlisted
    }
}
