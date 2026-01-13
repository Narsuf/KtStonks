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
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.test_data.data.getStockEntity
import java.util.*

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
    fun `getStocks should return empty list of stocks when empty list passed`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        val result = dao.getStocks(emptyList())

        assertEquals(emptyList<StockEntity>(), result)
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
        val updatedStock = stock.copy(
            symbol = "APL",
            companyName = "A",
            logo = StockEntity.Logo(Base64.getDecoder().decode("/9j/2wCEAAEBAQEBAQEBAQEBAQEC")),
            price = 200.0,
            dividendYield = 1.0,
            eps = 2.0,
            pe = 3.0,
            pb = 4.0,
            earningsQuarterlyGrowth = 5.0,
            currentIntrinsicValue = 6.0,
            currency = "EUR",
            lastUpdated = 7L,
            isWatchlisted = true,
        )

        dao.saveStock(updatedStock)

        assertEquals(updatedStock, dao.getStock("APL"))
    }

    @Test
    fun `saveStock with null values should not override existing values`() = runBlocking {
        val stock = getStockEntity(
            expectedEpsGrowth = 7.72,
            valuationFloor = 12.5,
            forwardIntrinsicValue = 100.58355,
            isWatchlisted = true,
        )
        dao.saveStock(stock)
        val updatedStock = stock.copy(
            logo = null,
            expectedEpsGrowth = null,
            valuationFloor = null,
            currentIntrinsicValue = null,
            forwardIntrinsicValue = null,
            isWatchlisted = false,

        )

        dao.saveStock(updatedStock)

        val result = dao.getStock("AAPL")
        assertEquals(stock.logo, result?.logo)
        assertEquals(stock.expectedEpsGrowth, result?.expectedEpsGrowth)
        assertEquals(stock.valuationFloor, result?.valuationFloor)
        assertEquals(stock.forwardIntrinsicValue, result?.forwardIntrinsicValue)
        assertEquals(stock.currentIntrinsicValue, result?.currentIntrinsicValue)
        assertEquals(stock.isWatchlisted, result?.isWatchlisted)
    }

    @Test
    fun `saveStock with null values should override existing values`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)
        val updatedStock = stock.copy(
            expectedEpsGrowth = 7.72,
            valuationFloor = 12.5,
        )

        dao.saveStock(updatedStock)

        val result = dao.getStock("AAPL")
        assertEquals(updatedStock.expectedEpsGrowth, result?.expectedEpsGrowth)
        assertEquals(updatedStock.valuationFloor, result?.valuationFloor)
        assertEquals(100.58355, result?.forwardIntrinsicValue)
        assertEquals(93.375, result?.currentIntrinsicValue)
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
