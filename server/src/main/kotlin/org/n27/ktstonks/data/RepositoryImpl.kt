package org.n27.ktstonks.data

import org.n27.ktstonks.data.db.stock.StockDao
import org.n27.ktstonks.data.json.SymbolReader
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.data.yfinance.mapping.toDomainEntity
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.extensions.isToday
import java.util.Base64

class RepositoryImpl(
    private val api: YfinanceApi,
    private val stockDao: StockDao,
    private val symbolReader: SymbolReader,
) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        val localStock = stockDao.getStock(symbol)
        val isStockUpdated = localStock?.lastUpdated?.isToday() ?: false

        if (isStockUpdated) {
            localStock
        } else {
            api.getStock(symbol)
                .toDomainEntity()
                .also { stockDao.saveStock(it) }
        }
    }

    override suspend fun updateStock(stock: Stock): Result<Unit> = runCatching { stockDao.saveStock(stock) }

    override suspend fun getStocks(
        page: Int,
        pageSize: Int,
        filterWatchlist: Boolean,
        symbol: String?,
    ): Result<Stocks> = runCatching {
        val params = symbolReader.getSymbols(symbol)
        val filteredParams = if (filterWatchlist) params.filterWatchlist() else params
        val paginatedParams = filteredParams
            .drop(page)
            .take(pageSize)

        val localStocks = stockDao.getStocks(paginatedParams)
        val remoteParams = paginatedParams
            .filter { param -> param !in localStocks.map { it.symbol } }
            .joinToString(separator = ",")

        val remoteStocks = getRemoteStocks(remoteParams)

        val nextPage = page + pageSize
        Stocks(
            items = localStocks + remoteStocks,
            nextPage = nextPage.takeIf { it <= filteredParams.size },
        )
    }

    private suspend fun List<String>.filterWatchlist(): List<String> {
        val watchlist = stockDao.getWatchlist(0, Int.MAX_VALUE).items.map { it.symbol }
        return filter { it !in watchlist }
    }

    private suspend fun getRemoteStocks(params: String, ignoreLogo: Boolean = false) = if (params.isNotEmpty()) {
        val stocks = api.getStocks(params)
        stocks.map { stock ->
            stock.toDomainEntity(
                logo = stock.logoUrl
                    ?.takeIf { !ignoreLogo }
                    ?.let { api.downloadImage(it) }
            ).also { stockDao.saveStock(it) }
        }
    } else {
        emptyList()
    }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching { stockDao.addToWatchlist(symbol) }

    override suspend fun getWatchlist(
        page: Int,
        pageSize: Int,
    ): Result<Stocks> = runCatching {
        val watchlist = stockDao.getWatchlist(page, pageSize)
        val stocksToUpdate = watchlist.items.filter { !it.lastUpdated.isToday() }
        val symbols = stocksToUpdate.joinToString(separator = ",") { it.symbol }
        val updatedStocks = getRemoteStocks(symbols, ignoreLogo = true)

        if (updatedStocks.isNotEmpty()) stockDao.saveStocks(updatedStocks)

        stockDao.getWatchlist(page, pageSize)
    }

    override suspend fun removeFromWatchlist(symbol: String): Result<Unit> = runCatching {
        stockDao.removeFromWatchlist(symbol)
    }
}
