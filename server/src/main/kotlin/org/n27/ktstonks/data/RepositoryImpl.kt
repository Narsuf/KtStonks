package org.n27.ktstonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.n27.ktstonks.MAX_CALLS_PER_DAY
import org.n27.ktstonks.data.alpha_vantage.AlphaVantageApi
import org.n27.ktstonks.data.alpha_vantage.mapping.toDomainEntity
import org.n27.ktstonks.data.alpha_vantage.mapping.toExpectedEpsGrowth
import org.n27.ktstonks.data.alpha_vantage.mapping.toPrice
import org.n27.ktstonks.data.db.api_usage.ApiUsageDao
import org.n27.ktstonks.data.db.stock.StockDao
import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.exceptions.ApiLimitExceededException
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Symbols
import java.time.LocalDate

class RepositoryImpl(
    private val api: AlphaVantageApi,
    private val apiUsageDao: ApiUsageDao,
    private val stockDao: StockDao,
) : Repository {

    override suspend fun getRemoteStock(symbol: String): Result<Stock> = runCatching {
        val today = LocalDate.now()
        val currentUsage = apiUsageDao.getUsage(today)

        if (currentUsage + 3 > MAX_CALLS_PER_DAY) {
            throw ApiLimitExceededException(
                "API call limit exceeded for today. Current usage: $currentUsage/$MAX_CALLS_PER_DAY"
            )
        }

        val stockResult = coroutineScope {
            val stockDeferred = async { api.getStock(symbol) }
            val quoteDeferred = async { api.getGlobalQuote(symbol) }
            val epsDeferred = async { api.getEpsEstimate(symbol) }

            val stock = stockDeferred.await()
            val globalQuote = quoteDeferred.await()
            val estimates = epsDeferred.await()

            stock.toDomainEntity(globalQuote.toPrice(), estimates.toExpectedEpsGrowth())
        }

        apiUsageDao.incrementUsage(today, 3)

        stockResult
    }

    override suspend fun getStoredStocks(
        page: Int,
        pageSize: Int,
    ): Result<Stocks> = runCatching { stockDao.getStocks(page, pageSize) }

    override suspend fun searchStoredStocks(
        symbol: String,
        page: Int,
        pageSize: Int,
    ): Result<Stocks> = runCatching { stockDao.searchStocks(symbol, page, pageSize) }

    override suspend fun getStoredStock(symbol: String): Result<Stock?> = runCatching { stockDao.getStock(symbol) }

    override suspend fun saveStock(stock: Stock): Result<Unit> = runCatching { stockDao.saveStock(stock) }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching { stockDao.addToWatchlist(symbol) }

    override suspend fun getWatchlist(
        page: Int,
        pageSize: Int,
    ): Result<Stocks> = runCatching { stockDao.getWatchlist(page, pageSize) }

    override suspend fun removeFromWatchlist(
        symbol: String,
    ): Result<Unit> = runCatching { stockDao.removeFromWatchlist(symbol) }

    override suspend fun getSymbols(): Result<Symbols> = runCatching { Symbols(JsonReader.getSymbols()) }
}
