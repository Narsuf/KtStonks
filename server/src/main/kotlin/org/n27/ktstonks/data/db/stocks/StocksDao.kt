package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
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

    private fun findStock(symbol: String) = StocksTable
        .select { StocksTable.symbol eq symbol }
        .map { it.toStockEntity() }
        .singleOrNull()

    suspend fun saveStock(stock: StockEntity) {
        dbQuery {
            val existingStock = findStock(stock.symbol)

            if (existingStock != null) {
                StocksTable.update(where = { StocksTable.symbol eq stock.symbol }) {
                    val valuationFloor = existingStock.valuationMeasures?.valuationFloor ?: stock.valuationMeasures?.valuationFloor
                    val intrinsicValue = if (valuationFloor != null)
                        stock.getIntrinsicValue(valuationFloor)
                    else
                        existingStock.valuationMeasures?.intrinsicValue

                    it.fromStockEntity(
                        stock.copy(
                            isWatchlisted = existingStock.isWatchlisted,
                            logo = existingStock.logo ?: stock.logo,
                            valuationMeasures = existingStock.valuationMeasures?.copy(
                                valuationFloor = valuationFloor,
                                intrinsicValue = intrinsicValue,
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
        this[StocksTable.eps] = stock.incomeStatement?.eps
        this[StocksTable.earningsQuarterlyGrowth] = stock.incomeStatement?.earningsQuarterlyGrowth
        this[StocksTable.revenueQuarterlyGrowth] = stock.incomeStatement?.revenueQuarterlyGrowth
        this[StocksTable.pe] = stock.valuationMeasures?.pe
        this[StocksTable.pb] = stock.valuationMeasures?.pb
        this[StocksTable.ps] = stock.valuationMeasures?.ps
        this[StocksTable.revenueEstimateGrowthLow] = stock.analysis?.revenueEstimate?.growthLow
        this[StocksTable.revenueEstimateGrowthHigh] = stock.analysis?.revenueEstimate?.growthHigh
        this[StocksTable.earningsEstimateGrowthLow] = stock.analysis?.earningsEstimate?.growthLow
        this[StocksTable.earningsEstimateGrowthHigh] = stock.analysis?.earningsEstimate?.growthHigh
        this[StocksTable.valuationFloor] = stock.valuationMeasures?.valuationFloor
        this[StocksTable.intrinsicValue] = stock.valuationMeasures?.intrinsicValue
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
        incomeStatement = run {
            val eps = this[StocksTable.eps]
            val earningsQuarterlyGrowth = this[StocksTable.earningsQuarterlyGrowth]
            val revenueQuarterlyGrowth = this[StocksTable.revenueQuarterlyGrowth]
            if (eps != null || earningsQuarterlyGrowth != null || revenueQuarterlyGrowth != null)
                StockEntity.IncomeStatement(eps, earningsQuarterlyGrowth, revenueQuarterlyGrowth)
            else null
        },
        analysis = run {
            val earningsLow = this[StocksTable.earningsEstimateGrowthLow]
            val earningsHigh = this[StocksTable.earningsEstimateGrowthHigh]
            val revenueLow = this[StocksTable.revenueEstimateGrowthLow]
            val revenueHigh = this[StocksTable.revenueEstimateGrowthHigh]
            if (earningsLow != null || earningsHigh != null || revenueLow != null || revenueHigh != null)
                StockEntity.Analysis(
                    earningsEstimate = if (earningsLow != null || earningsHigh != null)
                        StockEntity.Analysis.Estimate(earningsLow, earningsHigh)
                    else null,
                    revenueEstimate = if (revenueLow != null || revenueHigh != null)
                        StockEntity.Analysis.Estimate(revenueLow, revenueHigh)
                    else null,
                )
            else null
        },
        valuationMeasures = run {
            val pe = this[StocksTable.pe]
            val pb = this[StocksTable.pb]
            val ps = this[StocksTable.ps]
            val valuationFloor = this[StocksTable.valuationFloor]
            val intrinsicValue = this[StocksTable.intrinsicValue]
            if (pe != null || pb != null || ps != null || valuationFloor != null || intrinsicValue != null)
                StockEntity.ValuationMeasures(pe, pb, ps, valuationFloor, intrinsicValue)
            else null
        },
        currency = this[StocksTable.currency],
        lastUpdated = this[StocksTable.lastUpdated],
        isWatchlisted = this[StocksTable.isWatchlisted],
    )

    private fun StockEntity.getIntrinsicValue(
        valuationFloor: Double,
    ) = valuationMeasures?.ps?.let { (price ?: 0.0) * (valuationFloor / it) } ?: 0.0
}
