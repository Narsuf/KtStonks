package org.n27.ktstonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import org.n27.ktstonks.data.alpha_vantage.AlphaVantageApi
import org.n27.ktstonks.data.alpha_vantage.mapping.toDomainEntity
import org.n27.ktstonks.data.alpha_vantage.mapping.toExpectedEpsGrowth
import org.n27.ktstonks.data.alpha_vantage.mapping.toPrice
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.data.db.mappers.toStock
import org.n27.ktstonks.data.db.mappers.toStocks
import org.n27.ktstonks.data.db.tables.StockTable
import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Symbols

class RepositoryImpl(private val api: AlphaVantageApi) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        coroutineScope {
            val stockDeferred = async { api.getStock(symbol) }
            val quoteDeferred = async { api.getGlobalQuote(symbol) }
            val epsDeferred = async { api.getEpsEstimate(symbol) }

            val stock = stockDeferred.await()
            val globalQuote = quoteDeferred.await()
            val estimates = epsDeferred.await()

            stock.toDomainEntity(globalQuote.toPrice(), estimates.toExpectedEpsGrowth())
        }
    }

    override suspend fun getStocks(page: Int, pageSize: Int): Result<Stocks> = runCatching {
        dbQuery { StockTable.selectAll().toStocks(page, pageSize) }
    }

    override suspend fun searchStocks(symbol: String, page: Int, pageSize: Int): Result<Stocks> = runCatching {
        dbQuery {
            StockTable
                .select { (StockTable.symbol like "%$symbol%") or (StockTable.companyName like "%$symbol%") }
                .toStocks(page, pageSize)
        }
    }

    override suspend fun getDbStock(symbol: String): Result<Stock?> = runCatching {
        dbQuery {
            StockTable
                .select { StockTable.symbol eq symbol }
                .map { it.toStock() }
                .singleOrNull()
        }
    }

    override suspend fun saveStock(stock: Stock): Result<Unit> = runCatching {
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

    override suspend fun getWatchlist(page: Int, pageSize: Int): Result<Stocks> = runCatching {
        dbQuery {
            StockTable
                .select { StockTable.isWatchlisted eq true }
                .toStocks(page, pageSize)
        }
    }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching {
        dbQuery {
            StockTable.update(where = { StockTable.symbol eq symbol }) { it[isWatchlisted] = true }
        }
    }

    override suspend fun removeFromWatchlist(symbol: String): Result<Unit> = runCatching {
        dbQuery {
            StockTable.update(where = { StockTable.symbol eq symbol }) { it[isWatchlisted] = false }
        }
    }

    override suspend fun getSymbols(): Result<Symbols> = runCatching { Symbols(JsonReader.getSymbols()) }

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
