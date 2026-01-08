package org.n27.ktstonks.data

import org.n27.ktstonks.data.db.stocks.StocksDao
import org.n27.ktstonks.data.db.stocks.toEntity
import org.n27.ktstonks.data.db.stocks.toStock
import org.n27.ktstonks.data.db.stocks.toStocks
import org.n27.ktstonks.data.json.SymbolReader
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.data.yfinance.mapping.toDomainEntity
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock
import org.n27.ktstonks.extensions.isToday

class RepositoryImpl(
    private val api: YfinanceApi,
    private val stocksDao: StocksDao,
    private val symbolReader: SymbolReader,
) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        val localStock = stocksDao.getStock(symbol)
        val isStockUpdated = localStock?.lastUpdated?.isToday() ?: false

        if (isStockUpdated) {
            localStock.toStock()
        } else {
            api.getStock(symbol)
                .toDomainEntity()
                .also { stocksDao.saveStock(it.toEntity()) }
        }
    }

    override suspend fun updateStock(stock: Stock): Result<Unit> = runCatching { stocksDao.saveStock(stock.toEntity()) }

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

        val localStocks = stocksDao.getStocks(paginatedParams).map { it.toStock() }
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
        val watchlist = stocksDao.getWatchlist(0, Int.MAX_VALUE).items.map { it.symbol }
        return filter { it !in watchlist }
    }

    private suspend fun getRemoteStocks(params: String, ignoreLogo: Boolean = false) = if (params.isNotEmpty()) {
        val stocks = api.getStocks(params)
        stocks.map { stock ->
            stock.toDomainEntity(
                logo = stock.logoUrl
                    ?.takeIf { !ignoreLogo }
                    ?.let { api.downloadImage(it) }
            ).also { stocksDao.saveStock(it.toEntity()) }
        }
    } else {
        emptyList()
    }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching { stocksDao.addToWatchlist(symbol) }

    override suspend fun getWatchlist(
        page: Int,
        pageSize: Int,
    ): Result<Stocks> = runCatching {
        val watchlist = stocksDao.getWatchlist(page, pageSize)
        val stocksToUpdate = watchlist.items.filter { !it.lastUpdated.isToday() }
        val symbols = stocksToUpdate.joinToString(separator = ",") { it.symbol }
        val updatedStocks = getRemoteStocks(symbols, ignoreLogo = true)

        if (updatedStocks.isNotEmpty()) stocksDao.saveStocks(updatedStocks.map { it.toEntity() })

        stocksDao.getWatchlist(page, pageSize).toStocks()
    }

    override suspend fun removeFromWatchlist(symbol: String): Result<Unit> = runCatching {
        stocksDao.removeFromWatchlist(symbol)
    }
}
