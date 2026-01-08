package org.n27.ktstonks.data.db.stock

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.data.db.stock.StocksEntity.StockEntity

class StockDao {

    suspend fun getStocks(symbols: Collection<String>): List<StockEntity> = dbQuery {
        StocksTable
            .select { StocksTable.symbol inList symbols }
            .map { it.toStockEntity() }
    }

    suspend fun getStock(symbol: String): StockEntity? = dbQuery {
        StocksTable
            .select { StocksTable.symbol eq symbol }
            .map { it.toStockEntity() }
            .singleOrNull()
    }

    suspend fun saveStock(stock: StockEntity) {
        dbQuery {
            val existingStock = getStock(stock.symbol)

            if (existingStock != null) {
                StocksTable.update(where = { StocksTable.symbol eq stock.symbol }) {
                    it.fromStockEntity(
                        stock.copy(
                            isWatchlisted = existingStock.isWatchlisted,
                            logo = existingStock.logo ?: stock.logo
                        )
                    )
                }
            } else {
                StocksTable.insert {
                    it[symbol] = stock.symbol
                    it.fromStockEntity(stock)
                }
            }
        }
    }

    suspend fun saveStocks(stocks: List<StockEntity>) {
        if (stocks.isEmpty()) return

        dbQuery {
            val existingData = StocksTable
                .slice(StocksTable.symbol, StocksTable.logo, StocksTable.isWatchlisted)
                .select { StocksTable.symbol inList stocks.map { it.symbol } }
                .associate { row ->
                    row[StocksTable.symbol]
                        to (row[StocksTable.logo]?.let { StockEntity.Logo(it) }
                        to row[StocksTable.isWatchlisted])
                }

            StocksTable.batchUpsert(stocks) { stock ->
                this[StocksTable.symbol] = stock.symbol
                val existing = existingData[stock.symbol]
                val existingLogo = existing?.first
                val isWatchlisted = existing?.second ?: stock.isWatchlisted

                fromStockEntity(
                    stock.copy(
                        logo = existingLogo ?: stock.logo,
                        isWatchlisted = isWatchlisted
                    )
                )
            }
        }
    }

    suspend fun addToWatchlist(symbol: String) {
        dbQuery {
            StocksTable.update(where = { StocksTable.symbol eq symbol }) { it[isWatchlisted] = true }
        }
    }

    suspend fun getWatchlist(page: Int, pageSize: Int): StocksEntity = dbQuery {
        StocksTable
            .select { StocksTable.isWatchlisted eq true }
            .toStockEntities(page, pageSize)
    }

    suspend fun removeFromWatchlist(symbol: String) {
        dbQuery {
            StocksTable.update(where = { StocksTable.symbol eq symbol }) { it[isWatchlisted] = false }
        }
    }

    private fun <T> UpdateBuilder<T>.fromStockEntity(stock: StockEntity) {
        this[StocksTable.companyName] = stock.companyName
        this[StocksTable.logo] = stock.logo?.bytes
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

    private fun Query.toStockEntities(page: Int, pageSize: Int): StocksEntity = run {
        val stocks = count()
        val hasNextPage = (page + 1) * pageSize < stocks
        val offset = (page * pageSize).toLong()
        val nextPageValue = if (hasNextPage) page + 1 else null
        StocksEntity(
            items = limit(pageSize, offset).map { it.toStockEntity() },
            nextPage = nextPageValue,
        )
    }

    private fun ResultRow.toStockEntity() = StockEntity(
        symbol = this[StocksTable.symbol],
        companyName = this[StocksTable.companyName],
        logo = this[StocksTable.logo]?.let { StockEntity.Logo(it) },
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
