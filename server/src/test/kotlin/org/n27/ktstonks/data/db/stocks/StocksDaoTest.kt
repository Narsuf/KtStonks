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
            dividends = stock.dividends.copy(dividendYield = stock.dividends.dividendYield.copy(value = 1.0)),
            roe = stock.roe.copy(value = 2.5),
            profitMargin = stock.profitMargin.copy(value = 0.35),
            incomeStatement = stock.incomeStatement.copy(
                eps = stock.incomeStatement.eps.copy(value = 2.0),
                earningsQuarterlyGrowth = stock.incomeStatement.earningsQuarterlyGrowth.copy(value = 5.0),
            ),
            earningsEstimate = stock.earningsEstimate.copy(growthHigh = stock.earningsEstimate.growthHigh.copy(value = 20.0)),
            valuationMeasures = stock.valuationMeasures.copy(
                pe = stock.valuationMeasures.pe.copy(value = 3.0),
                valuationFloor = 8.0,
                intrinsicValue = 6.0,
            ),
            balanceSheet = stock.balanceSheet.copy(
                totalCashPerShare = stock.balanceSheet.totalCashPerShare.copy(value = 9.0),
                de = stock.balanceSheet.de.copy(value = 50.0),
            ),
            currency = "EUR",
            lastUpdated = 7L,
            isWatchlisted = true,
        )

        dao.saveStock(updatedStock)

        assertEquals(updatedStock, dao.getStock("APL"))
    }

    @Test
    fun `saveStock should compute variation when value changed`() = runBlocking {
        val stock = getStockEntity(roe = StockEntity.Metric(value = 100.0, variation = null))
        dao.saveStock(stock)
        val updatedStock = stock.copy(roe = stock.roe.copy(value = 120.0))

        dao.saveStock(updatedStock)

        assertEquals(20.0, dao.getStock("AAPL")?.roe?.variation)
    }

    @Test
    fun `saveStock should compute negative variation when value decreased`() = runBlocking {
        val stock = getStockEntity(roe = StockEntity.Metric(value = 100.0, variation = null))
        dao.saveStock(stock)
        val updatedStock = stock.copy(roe = stock.roe.copy(value = 80.0))

        dao.saveStock(updatedStock)

        assertEquals(-20.0, dao.getStock("AAPL")?.roe?.variation)
    }

    @Test
    fun `saveStock should keep previous variation when value did not change`() = runBlocking {
        val stock = getStockEntity(roe = StockEntity.Metric(value = 100.0, variation = null))
        dao.saveStock(stock)
        dao.saveStock(stock.copy(roe = stock.roe.copy(value = 120.0)))
        val unchangedStock = stock.copy(roe = stock.roe.copy(value = 120.0))

        dao.saveStock(unchangedStock)

        assertEquals(20.0, dao.getStock("AAPL")?.roe?.variation)
    }

    @Test
    fun `saveStock should keep variation null when inserting a new stock`() = runBlocking {
        val stock = getStockEntity(roe = StockEntity.Metric(value = 100.0, variation = null))

        dao.saveStock(stock)

        assertNull(dao.getStock("AAPL")?.roe?.variation)
    }

    @Test
    fun `saveStock should keep previous variation when new value is null`() = runBlocking {
        val stock = getStockEntity(roe = StockEntity.Metric(value = 100.0, variation = null))
        dao.saveStock(stock)
        dao.saveStock(stock.copy(roe = stock.roe.copy(value = 120.0)))
        val nullRoeStock = stock.copy(roe = stock.roe.copy(value = null))

        dao.saveStock(nullRoeStock)

        assertEquals(20.0, dao.getStock("AAPL")?.roe?.variation)
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
    fun `getWatchlistSymbols should return watchlisted symbols`() = runBlocking {
        val stock = getStockEntity(isWatchlisted = true)
        dao.saveStock(stock)

        val result = dao.getWatchlistSymbols()

        assertEquals(listOf(stock.symbol), result)
    }

    @Test
    fun `removeFromWatchlist should update isWatchlisted to false`() = runBlocking {
        val stock = getStockEntity(isWatchlisted = true)
        dao.saveStock(stock)

        dao.removeFromWatchlist("AAPL")

        assertEquals(false, dao.getStock("AAPL")?.isWatchlisted)
    }
}
