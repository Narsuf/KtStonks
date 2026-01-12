package org.n27.ktstonks.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.n27.ktstonks.data.db.stocks.StocksDao
import org.n27.ktstonks.data.json.SymbolReader
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.test_data.data.getStockEntity
import org.n27.ktstonks.test_data.data.getStockRaw
import org.n27.ktstonks.test_data.data.getStocksEntity
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.getStocks

class RepositoryImplTest {

    private lateinit var repository: Repository
    private lateinit var api: YfinanceApi
    private lateinit var stocksDao: StocksDao
    private lateinit var symbolReader: SymbolReader

    @Before
    fun setUp() {
        api = mock(YfinanceApi::class.java)
        stocksDao = mock(StocksDao::class.java)
        symbolReader = mock(SymbolReader::class.java)
        repository = RepositoryImpl(api, stocksDao, symbolReader)
    }

    @Test
    fun `getStock should return local stock if updated`() = runBlocking {
        val symbol = "AAPL"
        val now = System.currentTimeMillis()
        `when`(stocksDao.getStock(symbol)).thenReturn(getStockEntity(lastUpdated = now))

        val result = repository.getStock(symbol)

        assertEquals(getStock(lastUpdated = now), result.getOrNull())
    }

    @Test
    fun `getStock should return remote stock if not updated`() = runBlocking {
        val symbol = "AAPL"
        `when`(stocksDao.getStock(symbol)).thenReturn(null)
        `when`(api.getStock(symbol)).thenReturn(getStockRaw())

        val result = repository.getStock(symbol)

        assertEquals(
            getStock(
                logo = null,
                lastUpdated = result.getOrNull()!!.lastUpdated,
            ),
            result.getOrNull(),
        )
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
        val watchlist = getStocksEntity(items = listOf(getStockEntity(isWatchlisted = true)))
        `when`(symbolReader.getSymbols(null)).thenReturn(listOf("AAPL"))
        `when`(stocksDao.getWatchlist(anyInt(), anyInt())).thenReturn(watchlist)
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
    fun `getStocks should return remote stocks with query`() = runBlocking {
        `when`(symbolReader.getSymbols(anyString())).thenReturn(listOf("AAPL"))
        `when`(stocksDao.getStocks(anyList())).thenReturn(emptyList())
        `when`(api.getStocks(anyString())).thenReturn(listOf(getStockRaw()))

        val result = repository.getStocks(0, 1, false, "AAPL")

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
}
