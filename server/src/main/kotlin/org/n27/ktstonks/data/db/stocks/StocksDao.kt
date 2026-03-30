package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.*

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
                    val valuationFloor = stock.valuationMeasures.valuationFloor
                        ?: existingStock.valuationMeasures.valuationFloor
                        ?: 15.0

                    it.fromStockEntity(
                        stock.copy(
                            isWatchlisted = existingStock.isWatchlisted,
                            logo = existingStock.logo ?: stock.logo,
                            valuationMeasures = stock.valuationMeasures.copy(
                                valuationFloor = valuationFloor,
                                intrinsicValue = stock.getIntrinsicValue(valuationFloor),
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
        dividends = Dividends(
            dividendYield = this[StocksTable.dividendYield],
            payoutRatio = this[StocksTable.payoutRatio],
        ),
        roe = this[StocksTable.roe],
        profitMargin = this[StocksTable.profitMargin],
        incomeStatement = IncomeStatement(
            eps = this[StocksTable.eps],
            earningsQuarterlyGrowth = this[StocksTable.earningsQuarterlyGrowth],
        ),
        earningsEstimate = Estimate(
            growthHigh = this[StocksTable.earningsEstimateGrowthHigh],
            growthAvg = this[StocksTable.earningsEstimateGrowthAvg],
        ),
        valuationMeasures = ValuationMeasures(
            pe = this[StocksTable.pe],
            valuationFloor = this[StocksTable.valuationFloor],
            intrinsicValue = this[StocksTable.intrinsicValue],
        ),
        balanceSheet = BalanceSheet(
            totalCashPerShare = this[StocksTable.totalCashPerShare],
            de = this[StocksTable.de],
            currentRatio = this[StocksTable.currentRatio],
        ),
        currency = this[StocksTable.currency],
        lastUpdated = this[StocksTable.lastUpdated],
        isWatchlisted = this[StocksTable.isWatchlisted],
    )

    private fun StockEntity.getIntrinsicValue(
        valuationFloor: Double,
    ) = valuationMeasures.pe
        ?.takeIf { it > 0 }
        ?.let { (price ?: 0.0) * (valuationFloor / it) } ?: 0.0
}
