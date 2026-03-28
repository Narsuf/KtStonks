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
            incomeStatement = stock.incomeStatement.copy(eps = 2.0, earningsQuarterlyGrowth = 5.0),
            valuationMeasures = stock.valuationMeasures.copy(pe = 3.0, intrinsicValue = 6.0),
            currency = "EUR",
            lastUpdated = 7L,
            isWatchlisted = true,
        )

        dao.saveStock(updatedStock)

        assertEquals(updatedStock, dao.getStock("APL"))
    }

    @Test
    fun `getIntrinsicValue should calculate current intrinsic value based on pe and price`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        dao.saveStock(stock.copy(valuationMeasures = stock.valuationMeasures.copy(valuationFloor = 12.5)))

        val expected = 93.37500000000016
        assertEquals(expected, dao.getStock("AAPL")?.valuationMeasures?.intrinsicValue)
    }

    @Test
    fun `getIntrinsicValue with null pe should return 0`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        dao.saveStock(stock.copy(valuationMeasures = stock.valuationMeasures.copy(pe = null, valuationFloor = 12.5)))

        assertEquals(0.0, dao.getStock("AAPL")?.valuationMeasures?.intrinsicValue)
    }

    @Test
    fun `getIntrinsicValue with null price should return 0`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        dao.saveStock(stock.copy(price = null, valuationMeasures = stock.valuationMeasures.copy(valuationFloor = 12.5)))

        assertEquals(0.0, dao.getStock("AAPL")?.valuationMeasures?.intrinsicValue)
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
