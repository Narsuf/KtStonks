package org.n27.ktstonks.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.n27.ktstonks.data.db.stocks.StocksDao
import org.n27.ktstonks.data.json.SymbolReader
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.data.LogoApi
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.test_data.data.getStockEntity
import org.n27.ktstonks.test_data.data.getStockRaw
import org.n27.ktstonks.test_data.data.getStocksEntity
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.getStocks

class RepositoryImplTest {

    private lateinit var repository: Repository
    private lateinit var api: YfinanceApi
    private lateinit var logoApi: LogoApi
    private lateinit var stocksDao: StocksDao
    private lateinit var symbolReader: SymbolReader

    @Before
    fun setUp() {
        api = mock(YfinanceApi::class.java)
        logoApi = mock(LogoApi::class.java)
        stocksDao = mock(StocksDao::class.java)
        symbolReader = mock(SymbolReader::class.java)
        repository = RepositoryImpl(api, logoApi, stocksDao, symbolReader)
    }

    @Test
    fun `getStock should return local stock if updated`() = runBlocking {
        val now = System.currentTimeMillis()
        `when`(stocksDao.getStock(anyString())).thenReturn(getStockEntity(lastUpdated = now))

        val result = repository.getStock("AAPL")

        assertEquals(getStock(lastUpdated = now), result.getOrNull())
    }

    @Test
    fun `getStock should return remote stock if not updated`() = runBlocking {
        val now = System.currentTimeMillis()
        `when`(stocksDao.getStock(anyString())).thenReturn(getStockEntity(), getStockEntity(lastUpdated = now))
        `when`(api.getStock(anyString())).thenReturn(getStockRaw())

        val result = repository.getStock("AAPL")

        assertEquals(getStock(lastUpdated = now), result.getOrNull())
    }

    @Test
    fun `getStocks should return remote stocks`() = runBlocking {
        `when`(symbolReader.getSymbols(null)).thenReturn(listOf("AAPL"))
        `when`(stocksDao.getStocks(anyList())).thenReturn(emptyList())
        `when`(api.getStocks(anyString())).thenReturn(listOf(getStockRaw()))

        val result = repository.getStocks(0, 1, false, null)

        assertEquals(
            getStocks(
                items = listOf(
                    getStock(
                        logo = null,
                        lastUpdated = result.getOrNull()?.items[0]!!.lastUpdated,
                    ),
                ),
            ),
            result.getOrNull(),
        )
    }

    @Test
    fun `getStocks should return remote stocks filtered by watchlist`() = runBlocking {
        `when`(symbolReader.getSymbols(null)).thenReturn(listOf("AAPL"))
        `when`(stocksDao.getWatchlistSymbols()).thenReturn(listOf(getStockEntity().symbol))
        `when`(stocksDao.getStocks(emptyList())).thenReturn(emptyList())
        `when`(api.getStocks("")).thenReturn(emptyList())

        val result = repository.getStocks(0, 1, true, null)

        assertEquals(
            getStocks(
                items = emptyList(),
                nextPage = null,
            ),
            result.getOrNull(),
        )
    }

    @Test
    fun `updateStock should call dao`() = runBlocking {
        repository.updateStock(getStock())

        verify(stocksDao).saveStock(getStockEntity())
    }

    @Test
    fun `addToWatchlist should call dao`() = runBlocking {
        val symbol = "AAPL"

        repository.addToWatchlist(symbol)

        verify(stocksDao).addToWatchlist(symbol)
    }

    @Test
    fun `removeFromWatchlist should call dao`() = runBlocking {
        val symbol = "AAPL"

        repository.removeFromWatchlist(symbol)

        verify(stocksDao).removeFromWatchlist(symbol)
    }

    @Test
    fun `getWatchlist should return watchlist without remote call if all updated`() = runBlocking {
        val now = System.currentTimeMillis()
        val watchlist = getStocksEntity(items = listOf(getStockEntity(lastUpdated = now)))
        `when`(stocksDao.getWatchlist(anyInt(), anyInt())).thenReturn(watchlist)

        val result = repository.getWatchlist(0, 1)

        verify(api, never()).getStocks(anyString())
        assertEquals(
            getStocks(items = listOf(getStock(lastUpdated = now))),
            result.getOrNull(),
        )
    }

    @Test
    fun `getWatchlist should refresh outdated stocks from remote`() = runBlocking {
        val outdated = getStocksEntity(items = listOf(getStockEntity(lastUpdated = 0)))
        val updated = getStocksEntity(items = listOf(getStockEntity(lastUpdated = System.currentTimeMillis())))
        `when`(stocksDao.getWatchlist(anyInt(), anyInt())).thenReturn(outdated, updated)
        `when`(api.getStocks(anyString())).thenReturn(listOf(getStockRaw()))

        val result = repository.getWatchlist(0, 1)

        verify(api).getStocks(anyString())
        assertEquals(
            getStocks(items = listOf(getStock(lastUpdated = updated.items[0].lastUpdated))),
            result.getOrNull(),
        )
    }
}
