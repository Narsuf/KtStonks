package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.*
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity

class StocksDao {

    suspend fun getStocks(symbols: Collection<String>): List<StockEntity> {
        if (symbols.isEmpty()) return emptyList()

        return dbQuery {
            StocksTable
                .select { StocksTable.symbol inList symbols }
                .map { it.toStockEntity() }
        }
    }

    suspend fun getStock(symbol: String): StockEntity? = dbQuery { findStock(symbol) }

    suspend fun saveStock(stock: StockEntity) {
        dbQuery {
            val existingStock = findStock(stock.symbol)

            if (existingStock != null) {
                StocksTable.update(where = { StocksTable.symbol eq stock.symbol }) {
                    it.fromStockEntity(
                        stock.copy(
                            isWatchlisted = existingStock.isWatchlisted,
                            logo = existingStock.logo ?: stock.logo,
                            valuationMeasures = stock.valuationMeasures.copy(
                                valuationFloor = stock.valuationMeasures.valuationFloor ?: existingStock.valuationMeasures.valuationFloor,
                                intrinsicValue = stock.valuationMeasures.intrinsicValue ?: existingStock.valuationMeasures.intrinsicValue,
                            ),
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

    private fun findStock(symbol: String) = StocksTable
        .select { StocksTable.symbol eq symbol }
        .map { it.toStockEntity() }
        .singleOrNull()

    suspend fun getWatchlistSymbols(): List<String> = dbQuery {
        StocksTable
            .slice(StocksTable.symbol)
            .select { StocksTable.isWatchlisted eq true }
            .map { it[StocksTable.symbol] }
    }

    suspend fun getWatchlist(page: Int, pageSize: Int): StocksEntity = dbQuery {
        StocksTable
            .select { StocksTable.isWatchlisted eq true }
            .toStockEntities(page, pageSize)
    }

    suspend fun addToWatchlist(symbol: String) {
        dbQuery {
            StocksTable.update(where = { StocksTable.symbol eq symbol }) { it[isWatchlisted] = true }
        }
    }

    suspend fun removeFromWatchlist(symbol: String) {
        dbQuery {
            StocksTable.update(where = { StocksTable.symbol eq symbol }) { it[isWatchlisted] = false }
        }
    }

}
