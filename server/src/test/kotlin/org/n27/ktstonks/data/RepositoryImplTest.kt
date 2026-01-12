package org.n27.ktstonks.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.n27.ktstonks.data.db.stocks.StocksDao
import org.n27.ktstonks.data.db.stocks.toEntity
import org.n27.ktstonks.data.json.SymbolReader
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.data.yfinance.mapping.toDomainEntity
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.test_data.data.getStockEntity
import org.n27.ktstonks.test_data.data.getStockRaw
import org.n27.ktstonks.test_data.getStock

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
}
