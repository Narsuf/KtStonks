package org.n27.ktstonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.n27.ktstonks.data.alpha_vantage.AlphaVantageApi
import org.n27.ktstonks.data.alpha_vantage.mapping.toDomainEntity
import org.n27.ktstonks.data.alpha_vantage.mapping.toExpectedEpsGrowth
import org.n27.ktstonks.data.alpha_vantage.mapping.toPrice
import org.n27.ktstonks.data.db.dbQuery
import org.n27.ktstonks.data.db.mappers.toStocks
import org.n27.ktstonks.data.db.tables.StockTable
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks

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

    override suspend fun getStocks(): Result<Stocks> = runCatching {
        dbQuery {
            StockTable
                .selectAll()
                .toStocks()
        }
    }

    override suspend fun searchStocks(query: String): Result<Stocks> = runCatching {
        dbQuery {
            StockTable
                .select {
                    (StockTable.symbol like "%$query%") or (StockTable.companyName like "%$query%")
                }.toStocks()
        }
    }
}
