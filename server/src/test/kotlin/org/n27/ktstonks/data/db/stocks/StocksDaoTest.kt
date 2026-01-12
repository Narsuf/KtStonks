package org.n27.ktstonks.data.db.stocks

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.n27.ktstonks.test_data.data.getStockEntity

class StocksDaoTest {

    private lateinit var dao: StocksDao

    @Before
    fun setUp() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction { SchemaUtils.create(StocksTable) }
        dao = StocksDao()
    }

    @After
    fun tearDown() {
        transaction { SchemaUtils.drop(StocksTable) }
    }

    @Test
    fun `getStocks should return list of stocks`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        val result = dao.getStocks(listOf("AAPL"))

        assertEquals(listOf(stock), result)
    }

    @Test
    fun `getStock should return stock`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        val result = dao.getStock("AAPL")

        assertEquals(stock, result)
    }

    @Test
    fun `getStock should return null if stock not found`() = runBlocking {
        val result = dao.getStock("AAPL")

        assertNull(result)
    }

    @Test
    fun `saveStock should update existing stock`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)
        val updatedStock = stock.copy(price = 200.0)

        dao.saveStock(updatedStock)

        assertEquals(updatedStock, dao.getStock("AAPL"))
    }

    @Test
    fun `addToWatchlist should update isWatchlisted to true`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        dao.addToWatchlist("AAPL")

        assertEquals(true,  dao.getStock("AAPL")?.isWatchlisted)
    }

    @Test
    fun `getWatchlist should return watchlisted stocks`() = runBlocking {
        val stock = getStockEntity(isWatchlisted = true)
        dao.saveStock(stock)

        val result = dao.getWatchlist(0, 10)

        assertEquals(listOf(stock), result.items)
    }

    @Test
    fun `removeFromWatchlist should update isWatchlisted to false`() = runBlocking {
        val stock = getStockEntity(isWatchlisted = true)
        dao.saveStock(stock)

        dao.removeFromWatchlist("AAPL")

        assertEquals(false, dao.getStock("AAPL")?.isWatchlisted)
    }
}
