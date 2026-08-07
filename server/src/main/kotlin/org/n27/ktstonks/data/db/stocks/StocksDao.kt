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
                            roe = stock.roe.updated(existingStock.roe),
                            profitMargin = stock.profitMargin.updated(existingStock.profitMargin),
                            dividends = stock.dividends.copy(
                                dividendYield = stock.dividends.dividendYield.updated(existingStock.dividends.dividendYield),
                            ),
                            incomeStatement = stock.incomeStatement.copy(
                                eps = stock.incomeStatement.eps.updated(existingStock.incomeStatement.eps),
                                earningsQuarterlyGrowth = stock.incomeStatement.earningsQuarterlyGrowth
                                    .updated(existingStock.incomeStatement.earningsQuarterlyGrowth),
                            ),
                            earningsEstimate = stock.earningsEstimate.copy(
                                growthHigh = stock.earningsEstimate.growthHigh.updated(existingStock.earningsEstimate.growthHigh),
                            ),
                            valuationMeasures = stock.valuationMeasures.copy(
                                valuationFloor = stock.valuationMeasures.valuationFloor ?: existingStock.valuationMeasures.valuationFloor,
                                intrinsicValue = stock.valuationMeasures.intrinsicValue ?: existingStock.valuationMeasures.intrinsicValue,
                                pe = stock.valuationMeasures.pe.updated(existingStock.valuationMeasures.pe),
                            ),
                            balanceSheet = stock.balanceSheet.copy(
                                totalCashPerShare = stock.balanceSheet.totalCashPerShare.updated(existingStock.balanceSheet.totalCashPerShare),
                                de = stock.balanceSheet.de.updated(existingStock.balanceSheet.de),
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

    private fun StockEntity.Metric.updated(existing: StockEntity.Metric) =
        copy(variation = computeVariation(existing.value, value, existing.variation))

    private fun computeVariation(old: Double?, new: Double?, previous: Double?): Double? = when {
        new == null -> null
        old != null && old != new -> new - old
        else -> previous
    }

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
